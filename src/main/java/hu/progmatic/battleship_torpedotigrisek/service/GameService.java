package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    /*

    private final Board board;
@Autowired
    public GameService(Board board) {
        this.board = board;
    }

    public boolean placeShipOnboard(Ship ship) {

        int boardSize = board.getGrid().length; // Feltételezve, hogy a tábla négyzet alakú

        for (int i = 0; i < ship.getSize(); i++) {
            int x = ship.getStartX();
            int y = ship.getStartY();

            if ("HORIZONTAL".equals(ship.getOrientation())) {
                x += i;
            } else if ("VERTICAL".equals(ship.getOrientation())) {
                y += i;
            }

            // Ellenőrizzük, hogy a hajó koordinátái a tábla határain belül vannak-e
            if (x >= boardSize || y >= boardSize || x < 0 || y < 0) {
                return false; // A hajó kimegy a tábla szélén
            }

            // Ellenőrizzük, hogy a cella már foglalt-e
            if ("S".equals(board.getGrid()[y][x])) {
                return false; // A cella már foglalt egy másik hajó által
            }
        }

        // Ha minden ellenőrzés sikeres, elhelyezzük a hajót
        for (int i = 0; i < ship.getSize(); i++) {
            if ("HORIZONTAL".equals(ship.getOrientation())) {
                board.getGrid()[ship.getStartY()][ship.getStartX() + i] = "S";
            } else if ("VERTICAL".equals(ship.getOrientation())) {
                board.getGrid()[ship.getStartY() + i][ship.getStartX()] = "S";
            }
        }
        return true; // Sikeres hajó elhelyezés

    }

     */

}
