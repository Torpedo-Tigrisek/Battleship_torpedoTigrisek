package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
public class Board {

    private final int width = 10;
    private final int height = 10;
    private String[][] grid;

    public Board() {
        grid = new String[height][width];
        for (String[] row : grid) {
            Arrays.fill(row, " ");
        }
    }


}