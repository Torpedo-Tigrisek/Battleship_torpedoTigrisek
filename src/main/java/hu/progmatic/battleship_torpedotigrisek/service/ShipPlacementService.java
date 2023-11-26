package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Coordinate;
import hu.progmatic.battleship_torpedotigrisek.model.EnemyShip;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ShipPlacementService {
    private final Random random = new Random();
    private final Board board;

    @Autowired
    public ShipPlacementService(Board board) {
        this.board = board;
    }

    public boolean placeShipRandomly(Ship ship) {
        int tries = 0;
        while (tries < 100) {
            int startX, startY;
            boolean horizontal = random.nextBoolean();

            if (horizontal) {
                startX = random.nextInt(board.getWidth() - ship.getShipType().getSize() + 1);
                startY = random.nextInt(board.getHeight());
            } else {
                startX = random.nextInt(board.getWidth());
                startY = random.nextInt(board.getHeight() - ship.getShipType().getSize() + 1);
            }

            if (canPlaceShip(startX, startY, ship, horizontal)) {
                setShipPosition(ship, startX, startY, horizontal);
                return true;
            }
            tries++;
        }
        return false;
    }

    private void setShipPosition(Ship ship, int startX, int startY, boolean horizontal) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < ship.getShipType().getSize(); i++) {
            if (horizontal) {
                coordinates.add(new Coordinate(startX + i, startY));
            } else {
                coordinates.add(new Coordinate(startX, startY + i));
            }
        }
        ship.setCoordinates(coordinates);
        placeShipOnBoard(ship);
    }

    private void placeShipOnBoard(Ship ship) {
        for (Coordinate coord : ship.getCoordinates()) {
            board.getGrid()[coord.getY()][coord.getX()] = "S";
        }
    }


    private boolean canPlaceShip(int startX, int startY, Ship ship, boolean horizontal) {
        String[][] grid = board.getGrid();
        for (int i = 0; i < ship.getShipType().getSize(); i++) {
            int x = horizontal ? startX + i : startX;
            int y = horizontal ? startY : startY + i;

            if (!isCellValid(x, y) || isCellOccupied(x, y, grid)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCellValid(int x, int y) {
        return x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight();
    }

    private boolean isCellOccupied(int x, int y, String[][] grid) {
        return !grid[y][x].equals(" ");
    }

    /*
    public List<Ship> getPlacedShips() {
        List<Ship> placedShips = new ArrayList<>();
        // Itt végigiterálsz a táblán és összegyűjted a hajók koordinátáit
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String cellValue = grid[y][x];
                if ("S".equals(cellValue)) {
                    // Itt meg kell határozni, melyik hajóhoz tartozik ez a koordináta
                    // Ez a rész a kódod bővítését igényli, hogy nyomon követhesd a hajók egyedi azonosítóit
                    Ship ship = findShipByCoordinate(x, y);
                    if (ship != null && !placedShips.contains(ship)) {
                        placedShips.add(ship);
                    }
                }
            }
        }
        return placedShips;
    }
    private Ship findShipByCoordinate(int x, int y) {
        // Megkeresi a hajót a koordináta alapján
        // Ez a logika attól függ, hogyan tárolod a hajók információit
        // Például ha van egy Map<Coordinate, Ship> map-od, akkor:
        return shipMap.get(new Coordinate(x, y));
    }

     */

}



