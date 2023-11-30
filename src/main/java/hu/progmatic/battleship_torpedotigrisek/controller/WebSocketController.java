package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import hu.progmatic.battleship_torpedotigrisek.service.ShipPlacementService;
import hu.progmatic.battleship_torpedotigrisek.service.ShotService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class WebSocketController {

    private final Board playerBoard;
    private final Board enemyBoard;
    private List<Ship> ships;
    private ShotService shotService;
    private List<ShipType> remainingShips;
    private final ShipPlacementService shipPlacementService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(Board playerBoard, Board enemyBoard, ShotService shotService, ShipPlacementService shipPlacementService, SimpMessagingTemplate messagingTemplate) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.shipPlacementService = shipPlacementService;
        this.messagingTemplate = messagingTemplate;
        this.ships = new ArrayList<>();
        this.shotService = shotService;
        this.remainingShips = new ArrayList<>(Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER,ShipType.ATTACKER
        ));
    }


    @MessageMapping("/placeRandomShips")
    @SendTo("/topic/shipPlaced")
    public Board handleRandomShipPlacement(){
        resetGame();
        placeAllShips();
        sendShipData();

        for (Ship ship : ships) {
            System.out.println("Hajó elhelyezve: " + ship);
        }
        System.out.println(ships);

        return playerBoard;
    }

    private void sendShipData() {
        List<Map<String, Object>> shipData = new ArrayList<>();
        for (Ship ship : ships) {
            Map<String, Object> shipInfo = new HashMap<>();
            shipInfo.put("type", ship.getShipType().toString());
            shipInfo.put("coordinates", ship.getCoordinates());
            shipData.add(shipInfo);
        }
        messagingTemplate.convertAndSend("/topic/shipData", shipData);

    }

    private void placeAllShips() {
        // Hajókat újra lerakjuk a listából
        for (ShipType shipType : remainingShips) {
            Ship ship = new Ship(shipType, Math.random() < 0.5);
            if (shipPlacementService.placeShipRandomly(playerBoard, ship)){
                ships.add(ship);
            }
        }
    }

    private void resetGame() {
        // Hajólista törlése
        ships.clear();

        // Tábla törlése
        shipPlacementService.clearShips(playerBoard);
        shipPlacementService.clearShips(enemyBoard);

        resetRemainingShips();
    }

    private void resetRemainingShips() {
        remainingShips = Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER
        );
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
    @MessageMapping("battle.sendHit")
    @SendTo("/topic/public")
    public HitCoordinate sendHit(@Payload HitCoordinate hitCoordinate) {
        System.out.println(hitCoordinate.getHitCoordinates());
        return hitCoordinate;
    }

}