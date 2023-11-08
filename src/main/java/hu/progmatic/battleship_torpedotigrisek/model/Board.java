package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Data;

import java.util.Arrays;
@Data
public class Board {

    private final int width = 10;
    private final int height = 10;
    private String[][] grid;

    public Board() {
        grid = new String[height][width];
        for (String[] row : grid) {
            Arrays.fill(row, "~");
        }
    }

    public void placeShip(Ship ship) {
        for (int i = 0; i < ship.getSize(); i++) {
            grid[ship.getStartY()][ship.getStartX() + i] = "S";
        }
    }


}
