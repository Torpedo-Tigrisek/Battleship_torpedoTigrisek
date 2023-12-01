package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShipPlacementService {
    private final Random random = new Random();
    private List<Ship> placedShips;
    private List<Ship> placedEnemyShips;
    private boolean isShipsPlaced = false;


    public boolean placeShipRandomly(Board board, Ship ship) {
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

            if (canPlaceShip(board, startX, startY, ship, horizontal)) {
                setShipPosition(board, ship, startX, startY, horizontal);
                return true;
            }
            tries++;
        }
        return false;
    }

    private void setShipPosition(Board board, Ship ship, int startX, int startY, boolean horizontal) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < ship.getShipType().getSize(); i++) {
            if (horizontal) {
                coordinates.add(new Coordinate(startX + i, startY));
            } else {
                coordinates.add(new Coordinate(startX, startY + i));
            }
        }
        ship.setCoordinates(coordinates);
        placeShipOnBoard(board, ship);
    }

    private void placeShipOnBoard(Board board, Ship ship) {
        for (Coordinate coord : ship.getCoordinates()) {
            board.getGrid()[coord.getY()][coord.getX()] = "S";
        }
    }


    private boolean canPlaceShip(Board board, int startX, int startY, Ship ship, boolean horizontal) {
        int shipSize = ship.getShipType().getSize();

        for (int i = 0; i < shipSize; i++) {
            int x = horizontal ? startX + i : startX;
            int y = horizontal ? startY : startY + i;

            // Ellenőrizzük, hogy a hajó aktuális cellája érvényes-e és szabad-e
            if (!isCellValid(board, x, y) || isCellOccupied(board, x, y)) {
                return false;
            }

            // Ellenőrizzük a szomszédos cellákat
            int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
            int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};
            for (int j = 0; j < dx.length; j++) {
                int adjX = x + dx[j];
                int adjY = y + dy[j];
                if (isCellValid(board, adjX, adjY) && isCellOccupied(board, adjX, adjY)) {
                    return false; // A hajó nem helyezhető el, mert a szomszédos cella már foglalt
                }
            }
        }
        return true;
    }

    private boolean isCellValid(Board board, int x, int y) {
        return x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight();
    }

    private boolean isCellOccupied(Board board, int x, int y) {
        return !" ".equals(board.getGrid()[y][x]);
    }


    public void clearShips(Board board) {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                board.getGrid()[y][x] = " ";
            }
        }
    }

    public void fixShipPosition(List<Ship> shipsOnBoard, List<Ship> enemyShipsOnBoard) {
        this.placedShips = new ArrayList<>(shipsOnBoard);
        this.placedEnemyShips = new ArrayList<>(enemyShipsOnBoard);
        System.out.println(placedShips);
        System.out.println(placedEnemyShips);


    }

}





