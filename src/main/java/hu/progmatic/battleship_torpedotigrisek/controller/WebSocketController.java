package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import hu.progmatic.battleship_torpedotigrisek.service.ShipPlacementService;
import hu.progmatic.battleship_torpedotigrisek.service.ShotService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class WebSocketController {

    private final Board playerBoard;
    private List<Ship> ships;
    private ShotService shotService;
    private List<ShipType> remainingShips;
    private final ShipPlacementService shipPlacementService;
    public WebSocketController(Board playerBoard, ShotService shotService, ShipPlacementService shipPlacementService) {
        this.playerBoard = playerBoard;
        this.shipPlacementService = shipPlacementService;
        this.ships = new ArrayList<>();
        this.shotService = shotService;
        this.remainingShips = new ArrayList<>(Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER,ShipType.ATTACKER
        ));
    }


    @MessageMapping("/placeShip")
    @SendTo("/topic/shipPlaced")
    public Board handleShipPlacement(@Payload Ship ship) {
        if (remainingShips.contains(ship.getShipType())) {
            boolean placedSuccessfully = shipPlacementService.placeShipRandomly(ship);
            if (placedSuccessfully) {
                ships.add(ship);
                remainingShips.remove(ship.getShipType());
                System.out.println("Hajó elhelyezve: " + ship);
                return playerBoard;
            } else {
                System.out.println("Nem sikerült elhelyezni a hajót: " + ship);
                return null; // vagy valamilyen hibaüzenet küldése
            }
        } else {
            System.out.println("A hajó típusa már el lett helyezve vagy érvénytelen: " + ship.getShipType());
            return null;
        }
    }
    @MessageMapping("/placeRandomShips")
    @SendTo("/topic/shipPlaced")
    public Board handleRandomShipPlacement() {
        // Töröljük a korábban elhelyezett hajók listáját
        ships.clear();

        // Töröljük a hajókat a tábláról
        shipPlacementService.clearShips();

        remainingShips = Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER
        );


        // Újra elhelyezzük a hajókat random módon
        for (ShipType shipType : remainingShips) {
            Ship ship = new Ship(shipType, Math.random() < 0.5);
            if (shipPlacementService.placeShipRandomly(ship)) {
                ships.add(ship);
            }
        }




        // Loggoljuk a hajókat a konzolra (opcionális)
        for (Ship ship : ships) {
            System.out.println("Hajó elhelyezve: " + ship);
        }

        System.out.println(ships);

        // Visszatérünk az új táblával
        return playerBoard;
    }

    @MessageMapping("/updateCell")
    @SendTo("/topic/boardUpdate")
    public Board updateCell(CellUpdateRequest request) {
        int rowIndex = request.getRowIndex();
        int colIndex = request.getColIndex();
        String newValue = request.getNewValue();

        boolean success = playerBoard.updateCell(rowIndex, colIndex, newValue);

        if (success) {
            return playerBoard;
        } else {
            // Hiba kezelése
            return null;
        }
    }



    @MessageMapping("/battle/sendShot")
    @SendTo("/topic/public")
    public ShotCoordinate sendShot(@Payload ShotCoordinate shotCoordinate) {
        System.out.println(shotCoordinate.getCoordinates());
        return shotCoordinate;
    }

    @SubscribeMapping("/generatedShot")
    public ShotCoordinate sendGeneratedShot() throws Exception {
        ShotCoordinate generatedShot = shotService.randomGeneratedShot();
        System.out.println("generatedShot = " + generatedShot.toString());
        return generatedShot;
    }

}
