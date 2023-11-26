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
            int startX = random.nextInt(board.getWidth());
            int startY = random.nextInt(board.getHeight());
            boolean horizontal = random.nextBoolean();

            if (canPlaceShip(startX, startY, ship, horizontal)) {
                ship.setStartPosition(startX, startY);
                board.placeShip(ship);
                return true;
            }
            tries++;
        }
        return false;
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



