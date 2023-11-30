package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import hu.progmatic.battleship_torpedotigrisek.service.GameService;
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
    private GameService gameService;
    private ShotService shotService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(GameService gameService, ShotService shotService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.shotService = shotService;
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/placeRandomShips")
    @SendTo("/topic/shipPlaced")
    public Board handleRandomShipPlacement() {
        gameService.resetGame();
        gameService.placeAllShips();
        sendShipData();

        for (Ship ship : gameService.getGame().getShips()) {
            System.out.println("Hajó elhelyezve: " + ship);
        }
        System.out.println(gameService.getGame().getShips());

        return gameService.getGame().getPlayerBoard();
    }

    private void sendShipData() {
        List<Map<String, Object>> shipData = new ArrayList<>();
        for (Ship ship : gameService.getGame().getShips()) {
            Map<String, Object> shipInfo = new HashMap<>();
            shipInfo.put("type", ship.getShipType().toString());
            shipInfo.put("coordinates", ship.getCoordinates());
            shipData.add(shipInfo);
        }
        messagingTemplate.convertAndSend("/topic/shipData", shipData);

    }


    @MessageMapping("/battle/sendShot")
    @SendTo("/topic/public")
    public ShotCoordinate sendShot(@Payload ShotCoordinate shotCoordinate) {

        return shotCoordinate;
    }

    @SubscribeMapping("/generatedShot")
    public ShotCoordinate sendGeneratedShot() throws Exception {
        ShotCoordinate generatedShot = shotService.randomGeneratedShot();
        System.out.println("The computer generated this generatedShot = " + generatedShot.toString());
        return generatedShot;
    }

    @MessageMapping("battle.sendHit")
    @SendTo("/topic/public")
    public HitCoordinate sendHit(@Payload HitCoordinate hitCoordinate) {
        System.out.println("On the enemy board this coordinate was a hit: " + hitCoordinate.getHitCoordinates());
        gameService.getGame().setPlayerScore(gameService.getGame().getPlayerScore()+1);
        System.out.println("Player score is changed to:" + gameService.getGame().getPlayerScore());
        return hitCoordinate;
    }

}
