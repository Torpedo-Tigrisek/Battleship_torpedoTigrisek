package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Data;

import java.util.*;

@Data
public class Board {

    private final int width = 10;
    private final int height = 10;
    private String[][] grid;
    private boolean isActive;

    public Board() {
        grid = new String[height][width];
        for (String[] row : grid) {
            Arrays.fill(row, " ");
        }
    }
}