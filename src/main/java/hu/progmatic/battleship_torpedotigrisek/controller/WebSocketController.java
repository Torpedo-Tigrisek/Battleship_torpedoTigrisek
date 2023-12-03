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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
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
    public Board handleRandomShipPlacement(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        System.out.println("userId: " + userId);
        if (userId != null) {
            Game game = gameService.startNewGameForUser(userId);
            System.out.println("Game: " + game );
            gameService.resetGame(userId);
            gameService.placeAllShips(userId);
            sendShipData(userId);
            System.out.println(game.getShips());
            return game.getPlayerBoard();
        }

        return null;
    }

    @MessageMapping("/ready")
    public void handleReady(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        if (userId != null) {
            gameService.fixShipPositions(userId);
        }

    }

    private void sendShipData(Long userId) {
        Game game = gameService.getUserGame().get(userId);
        if (game != null) {
            List<Map<String, Object>> shipData = new ArrayList<>();
            for (Ship ship : game.getShips()) {
                Map<String, Object> shipInfo = new HashMap<>();
                shipInfo.put("type", ship.getShipType().toString());
                shipInfo.put("coordinates", ship.getCoordinates());
                shipData.add(shipInfo);
            }
            messagingTemplate.convertAndSend("/topic/shipData", shipData);
        }

    }


    public void GamePlay() {

    }

    @MessageMapping("/battle.sendShot")
    @SendTo("/topic/public")
    public ShotCoordinate sendShot(Principal principal, @Payload ShotCoordinate shotCoordinate) {
        Long userId = getUserIdFromPrincipal(principal);
        if (userId != null) {
            boolean hit = gameService.evaluatePlayerShot(shotCoordinate, userId);
            System.out.println("The shot was sent to the enemy's board: " + shotCoordinate.getCoordinates());
            System.out.println("was this shot a hit? " + hit);
            if (hit) {
                gameService.updatePlayerScore(userId);
                if (gameService.isGameFinished(userId)) {
                    sendEnd(userId);
                }
            }
            return shotCoordinate;
        }
        return null;
    }

    @SubscribeMapping("/generatedShot")
    public ShotCoordinate sendGeneratedShot(Principal principal) throws Exception {
        Long userId = getUserIdFromPrincipal(principal);
        if (userId != null) {
            ShotCoordinate generatedShot = shotService.randomGeneratedShot();
            boolean hit = gameService.evaluateGeneratedShot(generatedShot, userId);
            System.out.println("The computer generated this shot: " + generatedShot.toString());
            System.out.println("was this generated shot a hit? " + hit);
            if (hit) {
                gameService.updateEnemyScore(userId);
                if (gameService.isGameFinished(userId)) {
                    sendEnd(userId);
                }
            }
            return generatedShot;
        }
        return null;
    }

    @MessageMapping("battle.sendHit")
    @SendTo("/topic/public")
    public HitCoordinate sendHit(@Payload HitCoordinate hitCoordinate) {
        System.out.println("On the enemy board this coordinate was a hit: " + hitCoordinate.getHitCoordinates());
        return hitCoordinate;
    }

    private void sendEnd(Long userId) {
        String winner = gameService.whoIsTheWinner(userId);
        System.out.println(winner);
        messagingTemplate.convertAndSend("/topic/end", winner);
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            Object principalObj = authentication.getPrincipal();
            if (principalObj instanceof User) {
                User user = (User) principalObj;
                return user.getId();
            }
        }
        return null;
    }
}
