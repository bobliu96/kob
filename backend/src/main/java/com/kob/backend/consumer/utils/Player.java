package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    private boolean checkTailIncreasing(int steps) { //Check rounds
        if (steps <= 10) return true;
        return steps % 3 == 1;
    }
    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x,y));
        for (int d: steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if (!checkTailIncreasing( ++ step)) {
                res.remove(0);
            }
        }
        return res;
    }
}
