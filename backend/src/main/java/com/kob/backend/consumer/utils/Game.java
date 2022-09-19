package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer innerWallsCount;
    final private int[][] grid;
    final private static int[] dx = {-1,0,1,0}, dy = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer innerWallsCount) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.grid = new int[rows][cols];
    }

    public int[][] getGrid() {
        return grid;
    }

    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        grid[sx][sy] = 1;

        for (int i = 0; i < 4; i ++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >0 && y < this.cols && grid[x][y] == 0) {
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
}
