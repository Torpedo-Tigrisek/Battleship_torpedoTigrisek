package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebSocketController {

    private final Board playerBoard;
    private List<Ship> ships;

    public WebSocketController(Board playerBoard) {
        this.playerBoard = playerBoard;
        this.ships = new ArrayList<>();
    }

    /*
    @MessageMapping("/placeShip")
    @SendTo("/topic/shipPlaced")

    public Ship handleShipPlacement(Ship placement) {
        Ship newShip = new Ship(placement.getShipType(), placement.getStartX(), placement.getStartY(), placement.getOrientation());
        playerBoard.addShip(newShip);
        playerBoard.placeShip(newShip);
        ships.add(newShip);
        System.out.println(ships);

        // Itt visszaküldheted a hajó adatokat a kliensnek, ha szükséges
        return newShip;
    }

     */

    @MessageMapping("/placeShip")
    @SendTo("/topic/shipPlaced")
    public Board handleShipPlacement(Ship placement) {

        Ship newShip = new Ship(placement.getShipType(), placement.getStartX(), placement.getStartY(), placement.getOrientation());

        playerBoard.placeShip(newShip);
        ships.add(newShip);
        System.out.println(ships);
        return playerBoard;
    }


}
