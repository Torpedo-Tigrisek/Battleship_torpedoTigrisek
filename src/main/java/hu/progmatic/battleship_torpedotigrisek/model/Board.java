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
        // (BUBU) Save to DB
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

    public void placeEnemyShipsRandomly(List<EnemyShip> ships) {
        for (EnemyShip ship : ships) {
            placeRandomEnemyShip(ship);
        }
    }

    public boolean placeRandomEnemyShip(EnemyShip ship) {
        int tries = 0;
        while (tries < 100) {
            int row = (int) (Math.random() * 10);
            int col = (int) (Math.random() * 10);
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL

            if (canPlaceEnemyShip(row, col, ship, orientation)) {
                ship.setOrientation(orientation);

                for (int i = 0; i < ship.getSize(); i++) {
                    if (orientation) {
                        ship.addCoordinate(row, col + i);
                    } else {
                        ship.addCoordinate(row + i, col);
                    }
                }

                placeEnemyShip(ship); // Elhelyezzük a hajót a táblán
                addShip(ship); // Hozzáadjuk az EnemyShip-et a shipMap-hez

                return true;
            }
            tries++;
        }
        return false;
    }


    public boolean canPlaceEnemyShip(int row, int col, EnemyShip ship, boolean orientation) {
        List<Coordinate> coordinates = ship.getCoordinates();

        for (int i = 0; i < ship.getSize(); i++) {
            int x = row;
            int y = col;

            if (orientation) {
                y += i;
            } else {
                x += i;
            }

            if (!isCellValid(x, y) || !grid[x][y].equals(" ")) {
                return false;
            }
        }
        return true;
    }

    public Map<ShipType, EnemyShip> getShipMap() {
        return shipMap;
    }
}