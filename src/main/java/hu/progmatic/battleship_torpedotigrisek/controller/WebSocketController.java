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

    private final Board playerBoard = new Board();
        @MessageMapping("/placeShip")
        @SendTo("/topic/shipPlaced")
        public Ship placeShip(Ship ship) {


            List<Ship> ships = new ArrayList<>();
            playerBoard.placeShip(ship);
            System.out.println("Location: " + ship);
            ships.add(ship);
            System.out.println(ships);


            return ship;
        }
}
