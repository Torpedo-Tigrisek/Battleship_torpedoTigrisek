package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import hu.progmatic.battleship_torpedotigrisek.model.ShipType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipPlacementServiceTest {

    @Test
    void placeShipRandomly() {
        // Létrehozunk egy új táblát
        Board board = new Board();

        // Létrehozunk egy új hajót (például 3 méretű)
        Ship ship = new Ship(ShipType.DESTROYER, true); // feltételezve, hogy a DESTROYER mérete 3

        // Létrehozzuk a ShipPlacementService példányt
        ShipPlacementService service = new ShipPlacementService();

        // Megpróbáljuk elhelyezni a hajót
        boolean result = service.placeShipRandomly(board, ship);

        // A teszt sikeres, ha a hajót sikerült elhelyezni
        assertTrue(result);
    }

    @Test
    void clearShips() {
    }

    @Test
    void fixShipPosition() {
    }

    @Test
    void areShipsPlaced() {
    }
}