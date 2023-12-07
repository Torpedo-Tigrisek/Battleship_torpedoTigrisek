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
    public Board handleRandomShipPlacement(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        System.out.println("userId: " + userId);
        if (userId != null) {
            Game game = gameService.getUserGame().get(userId);
            System.out.println("Game: " + game );

            gameService.resetGame(userId);
            gameService.placeAllShips(userId);
            sendShipData(userId);
            System.out.println(game.getShips());
            return game.getPlayerBoard();
        }

        return null;
    }
    /*
    @MessageMapping("/placeEnemyShips")
    public void handleEnemyShipPlacement(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        if (userId != null) {
            Game game = gameService.getUserGame().get(userId);
            if (game != null) {
                gameService.initializeEnemyShips(userId);
                System.out.println("Enemy ships placed for user: " + userId);
            }
        }
    }

     */


    @MessageMapping("/ready")
    public void handleReady(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        if (userId != null) {
            gameService.fixShipPositions(userId);
        }
        // ide kellene rakni, hogy a mapbe felülírja a hajókat a játékot és mentse?

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


    public void GamePlay(){

    }

    @MessageMapping("/battle.sendShot")
    @SendTo("/topic/public")
    public ShotCoordinate sendShot(Principal principal, @Payload ShotCoordinate shotCoordinate) {
        Long userId = getUserIdFromPrincipal(principal);

        System.out.println("The shot was sent to the enemy's board: " + shotCoordinate.getCoordinates());
        System.out.println("was this shot a hit? " + gameService.evaluatePlayerShot(shotCoordinate, userId));
        if (userId != null) {
            Game game = gameService.getUserGame().get(userId);
            boolean hit = gameService.evaluatePlayerShot(shotCoordinate, userId);
            if (hit) {
                game.setPlayerScore(game.getPlayerScore() + 1);
                System.out.println("Player " + game.getPlayerScore() + " : " + game.getEnemyScore() + " Enemy");
            }
            return shotCoordinate;
        }
        return null;
    }

    @SubscribeMapping("/generatedShot")
    public ShotCoordinate sendGeneratedShot(Principal principal) throws Exception {
        Long userId = getUserIdFromPrincipal(principal);
        ShotCoordinate generatedShot = gameService.randomGeneratedShot(userId);
        System.out.println("The computer generated this generatedShot = " + generatedShot.toString());
        System.out.println("was this generated shot a hit?" + gameService.evaluateGeneratedShot(generatedShot, userId));
        if (userId != null) {
            Game game = gameService.getUserGame().get(userId);

            boolean hit = gameService.evaluateGeneratedShot(generatedShot, userId);
            if (hit) {
                game.setEnemyScore(game.getEnemyScore() + 1);
                System.out.println("Player " + game.getPlayerScore() + " : " + game.getEnemyScore() + " Enemy");
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



    @SubscribeMapping("/end")
    public String sendEnd(Principal principal){
        Long userId = getUserIdFromPrincipal(principal);
        System.out.println(gameService.whoIsTheWinner(userId));
        if (gameService.whoIsTheWinner(userId).equals("You win")) {
            addScoreToPrincipal(principal);
            return gameService.whoIsTheWinner(userId);
        } else if (gameService.whoIsTheWinner(userId).equals("You lose")){
            addLossToPrincipal(principal);
            return gameService.whoIsTheWinner(userId);
        }
        return null;
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
                double winloss = gameService.winLossRation(user.getUserProfile().getWins(), user.getUserProfile().getLosses());
                user.getUserProfile().setWinLossRatio(winloss);
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
                Long userId = ((User) principalObj).getId();
                User user = userService.getUserById(userId).orElse(null);
                user.getUserProfile().setLosses(user.getUserProfile().getLosses() + 1);
                double winloss = gameService.winLossRation(user.getUserProfile().getWins(), user.getUserProfile().getLosses());
                user.getUserProfile().setWinLossRatio(winloss);
                userProfileService.addUserProfile(user.getUserProfile());
            }
        }
    }
}
