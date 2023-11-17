package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class Board {

    private final int width = 10;
    private final int height = 10;
    private String[][] grid;
    private Map<ShipType,Ship> shipMap= new HashMap<>();

    public Board() {
        grid = new String[height][width];
        for (String[] row : grid) {
            Arrays.fill(row, "~");
        }
    }


    public void placeShip(Ship ship) {
        if ("HORIZONTAL".equals(ship.getOrientation())) {
            for (int i = 0; i < ship.getSize(); i++) {
                grid[ship.getStartY()][ship.getStartX() + i] = "S";
            }
        } else if ("VERTICAL".equals(ship.getOrientation())) {
            for (int i = 0; i < ship.getSize(); i++) {
                grid[ship.getStartY() + i][ship.getStartX()] = "S";
            }
        }
    }

    public void addShip(Ship ship){
        shipMap.put(ship.getShipType(),ship);

    }


}
