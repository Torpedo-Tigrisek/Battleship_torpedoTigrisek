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
    private Map<ShipType, EnemyShip> shipMap = new HashMap<>();

    public Board() {
        grid = new String[height][width];
        for (String[] row : grid) {
            Arrays.fill(row, " ");
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

    public void placeEnemyShip(EnemyShip ship) {
        for (Coordinate coordinate : ship.getCoordinates()) {
            int x = coordinate.getX();
            int y = coordinate.getY();
            grid[x][y] = "S";
        }
    }

    public void addShip(EnemyShip ship) {
        shipMap.put(ship.getShipType(), ship);
    }

    public boolean updateCell(int rowIndex, int colIndex, String newValue) {
        if (isCellValid(rowIndex, colIndex)) {
            grid[rowIndex][colIndex] = newValue;
            return true;
        }
        return false;
    }

    private boolean isCellValid(int rowIndex, int colIndex) {
        return rowIndex >= 0 && rowIndex < height && colIndex >= 0 && colIndex < width;
    }

    public void placeShipsRandomly(List<EnemyShip> ships) {
        for (EnemyShip ship : ships) {
            placeRandomShip(ship);
        }
    }

    public boolean placeRandomShip(EnemyShip ship) {
        int tries = 0;
        while (tries < 100) {
            int row = (int) (Math.random() * 10);
            int col = (int) (Math.random() * 10);
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL

            if (canPlaceShip(row, col, ship, orientation)) {
                ship.setOrientation(orientation);

                for (int i = 0; i < ship.getSize(); i++) {
                    if (orientation) {
                        ship.addCoordinate(row, col + i);
                    } else {
                        ship.addCoordinate(row + i, col);
                    }
                }

                placeEnemyShip(ship);
                return true;
            }
            tries++;
        }
        return false;
    }

    public boolean canPlaceShip(int row, int col, EnemyShip ship, boolean orientation) {
        List<Coordinate> coordinates = ship.getCoordinates();

        for (Coordinate coordinate : coordinates) {
            int x = coordinate.getX();
            int y = coordinate.getY();

            if (!isCellValid(x, y) || !grid[x][y].equals(" ")) {
                return false;
            }
        }

        return true;
    }
}