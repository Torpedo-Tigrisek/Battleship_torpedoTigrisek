package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import hu.progmatic.battleship_torpedotigrisek.service.GameService;
import hu.progmatic.battleship_torpedotigrisek.service.UserProfileService;
import hu.progmatic.battleship_torpedotigrisek.service.UserService;
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
    private UserService userService;
    private UserProfileService userProfileService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(GameService gameService, UserService userService, UserProfileService userProfileService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/placeRandomShips")
    @SendTo("/topic/shipPlaced")
    public Board handleRandomShipPlacement() {
        gameService.resetGame();
        gameService.placeAllShips();
        sendShipData();

        System.out.println(gameService.getGame().getShips());

        return gameService.getGame().getPlayerBoard();
    }

    @MessageMapping("/ready")
    public void handleReady() {
        gameService.fixShipPositions(gameService.getGame().getShips(), gameService.getGame().getEnemyShips());

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

    public void GamePlay() {

    }

    @MessageMapping("/battle.sendShot")
    @SendTo("/topic/public")
    public ShotCoordinate sendShot(@Payload ShotCoordinate shotCoordinate) {
        System.out.println("The shot was sent to the enemy's board: " + shotCoordinate.getCoordinates());
        System.out.println("was this shot a hit?" + gameService.evaluatePlayerShot(shotCoordinate));
        boolean hit = gameService.evaluatePlayerShot(shotCoordinate);
        if (hit) {
            gameService.getGame().setPlayerScore(gameService.getGame().getPlayerScore() + 1);
            System.out.println("Player " + gameService.getGame().getPlayerScore() + " : " + gameService.getGame().getEnemyScore() + " Enemy");
        }
        return shotCoordinate;
    }

    @SubscribeMapping("/generatedShot")
    public ShotCoordinate sendGeneratedShot() throws Exception {
        ShotCoordinate generatedShot = gameService.randomGeneratedShot();
        System.out.println("The computer generated this generatedShot = " + generatedShot.toString());
        System.out.println("was this generated shot a hit?" + gameService.evaluateGeneratedShot(generatedShot));
        boolean hit = gameService.evaluateGeneratedShot(generatedShot);
        if (hit) {
            gameService.getGame().setEnemyScore(gameService.getGame().getEnemyScore() + 1);
            System.out.println("Player " + gameService.getGame().getPlayerScore() + " : " + gameService.getGame().getEnemyScore() + " Enemy");
        }
        return generatedShot;
    }

    @MessageMapping("battle.sendHit")
    @SendTo("/topic/public")
    public HitCoordinate sendHit(@Payload HitCoordinate hitCoordinate) {
        System.out.println("On the enemy board this coordinate was a hit: " + hitCoordinate.getHitCoordinates());
        return hitCoordinate;
    }

    @SubscribeMapping("/end")
    public String sendEnd(Principal principal) {
        System.out.println(gameService.whoIsTheWinner());
        if (gameService.whoIsTheWinner().equals("You win")) {
            addScoreToPrincipal(principal);
        } else {
            addLossToPrincipal(principal);
        }
        return gameService.whoIsTheWinner();
    }

    private void addScoreToPrincipal(Principal principal) {
        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            Object principalObj = authentication.getPrincipal();
            if (principalObj instanceof User) {
                Long userId = ((User) principalObj).getId();
                User user = userService.getUserById(userId).orElse(null);
                System.out.println("The score before winning: " + user.getUserProfile().getScore());
                user.getUserProfile().setScore(user.getUserProfile().getScore() + 20);
                user.getUserProfile().setWins(user.getUserProfile().getWins() + 1);
                userProfileService.addUserProfile(user.getUserProfile());
                System.out.println("And after winning: " + user.getUserProfile().getScore());
            }
        }
    }

    private void addLossToPrincipal(Principal principal) {
        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            Object principalObj = authentication.getPrincipal();
            if (principalObj instanceof User) {
                User user = (User) principalObj;
                user.getUserProfile().setLosses(user.getUserProfile().getLosses() + 1);
                userProfileService.addUserProfile(user.getUserProfile());
            }
        }
    }
}
