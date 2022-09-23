package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer innerWallsCount;
    private final int[][] grid;
    private final static int[] dx = {-1,0,1,0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing"; // playing -> finished
    private String loser = ""; // all: draw, A: A lose, B: B lose

    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer innerWallsCount, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.grid = new int[rows][cols];

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        playerA = new Player(idA, botIdA, botCodeA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB, 1, cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA (Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB (Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        grid[sx][sy] = 1;

        for (int i = 0; i < 4; i ++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >=0 && y < this.cols && grid[x][y] == 0) {
                if (checkConnectivity(x,y,tx,ty)) {
                    grid[sx][sy] = 0;
                    return true;
                }
            }
        }
        grid[sx][sy] = 0;
        return false;
    }

    public boolean drawGrid() {
        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.cols; j++) {
                grid[i][j] = 0;
            }
        }

        for (int r=0; r<this.rows; r++) {
            grid[r][0] = grid[r][this.cols-1] = 1;
        }
        for (int c=0; c<this.cols; c++) {
            grid[0][c] = grid[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i=0; i<this.innerWallsCount /2; i++) {
            for (int j=0; j<1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (grid[r][c] == 1 || grid[this.rows-1-r][this.cols-1-c] == 1)
                    continue;
                if (r == this.rows - 2 && c==1 || r == 1 && c == this.cols - 2)
                    continue;

                grid[r][c] = grid[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return checkConnectivity(this.rows - 2, 1, 1, this.cols-2);
    }

    public void createGrid() {
        for (int i = 0; i <1000; i++) {
            if (drawGrid())
                break;
        }
    }

    private String getGameData(Player player) {
        Player self, opponent;

        if (playerA.getId().equals(player.getId())){
            self = playerA;
            opponent = playerB;
        } else {
            self = playerB;
            opponent = playerA;
        }

        return getMapString() + "#" +
                self.getSx() + "#" +
                self.getSy() + "#(" +
                self.getStepsString() + ")#" +
                opponent.getSx() + "#" +
                opponent.getSy() + "#(" +
                opponent.getStepsString() + ")";
    }

    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1)) return; // User operation, no need to send code
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getGameData(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }
    private boolean nextStep() { //Wait for next step
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i ++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (grid[cell.x][cell.y] == 1) return false;

        for (int i = 0; i < n - 1; i ++) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }

        for (int i = 0; i < n-1; i ++) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }

        return true;
    }
    private void judge() { //judge users' next step valid or not
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }
    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }
    private void sendMove() { // send move command to user
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }
    private void sendResult() { //send result to client
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(grid[i][j]);
            }
        }
        return res.toString();
    }
    private void saveToDatabase() {
        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        Integer winA = WebSocketServer.userMapper.selectById(playerA.getId()).getWin();
        Integer winB = WebSocketServer.userMapper.selectById(playerB.getId()).getWin();

        Integer loseA = WebSocketServer.userMapper.selectById(playerA.getId()).getLose();
        Integer loseB = WebSocketServer.userMapper.selectById(playerB.getId()).getLose();

        Integer drawA = WebSocketServer.userMapper.selectById(playerA.getId()).getDraw();
        Integer drawB = WebSocketServer.userMapper.selectById(playerB.getId()).getDraw();

        if ("A".equals(loser)) {
            ratingA -= 2;
            ratingB += 5;

            winB += 1;
            loseA += 1;

        } else if ("B".equals(loser)) {
            ratingA += 5;
            ratingB -= 2;

            winA += 1;
            loseB += 1;
        } else {
            drawA += 1;
            drawB += 1;
        }

        updateUserRating(playerA, ratingA);
        updateUserRating(playerB, ratingB);

        updateUserGameRecord(playerA, winA, loseA, drawA);
        updateUserGameRecord(playerB, winB, loseB, drawB);

        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    private void updateUserRating(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    private void updateUserGameRecord(Player player, Integer win, Integer lose, Integer draw) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setWin(win);
        user.setLose(lose);
        user.setDraw(draw);
        WebSocketServer.userMapper.updateById(user);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i ++) {
            if (nextStep()) {
                judge();
                if (status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else if (nextStepB == null) {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
