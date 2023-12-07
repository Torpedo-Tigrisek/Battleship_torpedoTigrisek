package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.*;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.*;

@Service
@Data
public class GameService {
    private Game game;
    private ShipPlacementService shipPlacementService;
    private Map<Long, Game> userGame = new HashMap<>();

    public GameService(ShipPlacementService shipPlacementService) {

        this.shipPlacementService = shipPlacementService;

    }

    public void startNewGameForUser(Long userId) {

            if (userId != null) {
                Game game = newGame(userId);
                userGame.put(userId, game);
                logMapState("startNewGameForUser");
                System.out.println("New game started for user ID: " + userId + ", Game: " + game);
                initializeEnemyShips(userId);
            }
        }

        private void logMapState (String startNewGameForUser){
            System.out.println("Map state after " + startNewGameForUser + ": " + userGame);
        }


    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            User user = (User) authentication.getPrincipal();
            System.out.println("Current userId: " + user.getId());
            return user.getId();
        }
        return null;
    }


    public Game newGame(Long userId) {
        Game game = new Game();
        game.setPlayerBoard(new Board());
        game.setEnemyBoard(new Board());
        game.setPlayerScore(0);
        game.setEnemyScore(0);
        game.setShipTypes(new ShipType[]{ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER});
        game.setShips(new ArrayList<>());
        game.setEnemyShips(game.getEnemyShips());
        game.setRemainingShips(new ArrayList<>(Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER)));
        game.setAlreadyGeneratedShots(new ArrayList<>());
        return game;
    }

        public void initializeEnemyShips (Long userId){
            Game game = userGame.get(userId);
            if (game != null) {
                if (game.getEnemyShips() == null) {
                    game.setEnemyShips(new ArrayList<>());
                }

                List<Ship> enemyShips = generateShips(userId);

                for (Ship ship : enemyShips) {
                    if (shipPlacementService.placeShipRandomly(game.getEnemyBoard(), ship)) {
                        game.getEnemyShips().add(ship);
                    }
                }

                System.out.println("Enemy ships:" + game.getEnemyShips());
            } else {
                System.out.println("Game not initialized for user");
            }
        }



    public List<Ship> generateShips(Long userId) {

            Game game = userGame.get(userId);

        List<Ship> ships = new ArrayList<>();
        for (ShipType type : game.getShipTypes()) {
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL
            ships.add(new Ship(type, orientation));
        }
        return ships;
    }

        public void placeAllShips (Long userId){

        Game game = userGame.get(userId);
        for (ShipType shipType : game.getRemainingShips()) {
            Ship ship = new Ship(shipType, Math.random() < 0.5);
            if (shipPlacementService.placeShipRandomly(game.getPlayerBoard(), ship)) {
                game.getShips().add(ship);
            }
        }
    }

        public void resetGame (Long userId){

            Game game = userGame.get(userId);
            game.getShips().clear();

            shipPlacementService.clearShips(game.getPlayerBoard());
            shipPlacementService.clearShips(game.getEnemyBoard());

            resetRemainingShips(userId);
        }

        public void resetRemainingShips (Long userId){

            Game game = userGame.get(userId);
            game.setRemainingShips(Arrays.asList(
                    ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                    ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                    ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER
            ));
        }

    public void fixShipPositions(Long userId) {
        Game game = userGame.get(userId);
        if (game != null) {
            shipPlacementService.fixShipPosition(game.getShips(), game.getEnemyShips());
        }
    }

        public String whoIsTheWinner (Long userId){

            Game game = userGame.get(userId);
            if (game.getPlayerScore() == 20) {
                return "You win";
            } else if (game.getEnemyScore() == 20) {
                return "You lose";
            }
            return null;
        }

    public boolean isGameFinished(Long userId) {
        Game game = userGame.get(userId);
        return game != null && (game.getPlayerScore() == 20 || game.getEnemyScore() == 20);
    }

    public void removeUserGame(Long userId) {
        if (userId != null && userGame.containsKey(userId)) {
            userGame.remove(userId);
            System.out.println("Game for user ID " + userId + " removed.");
        }
    }


    public boolean evaluatePlayerShot(ShotCoordinate shotCoordinate, Long userId) {

        Game game = userGame.get(userId);
        int y = Integer.parseInt(shotCoordinate.getCoordinates().get(0).substring(0, 1));
        int x = Integer.parseInt(shotCoordinate.getCoordinates().get(0).substring(3));
        for (Ship actual : game.getEnemyShips()) {
            for (int i = 0; i < actual.getCoordinates().size(); i++) {
                if ((actual.getCoordinates().get(i).getX() == x) && (actual.getCoordinates().get(i).getY() == y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ShotCoordinate randomGeneratedShot(Long userId) {
        Game game = userGame.get(userId);
        ShotCoordinate shot = new ShotCoordinate();
        Random randomGenerator = new Random();
        do{
            List<String> shotArray = new ArrayList<>();
            String x = String.valueOf(randomGenerator.nextInt(0, 10));
            String y = String.valueOf(randomGenerator.nextInt(0, 10));
            shotArray.add(x);
            shotArray.add(y);
            shot.setCoordinates(shotArray);
        }while(isGeneratedShotAlreadyBeenShot(shot, userId));
        game.getAlreadyGeneratedShots().add(shot);
        return shot;
    }


        public boolean evaluateGeneratedShot (ShotCoordinate generatedShot, Long userId){

            Game game = userGame.get(userId);
            int y = Integer.parseInt(generatedShot.getCoordinates().get(0));
            int x = Integer.parseInt(generatedShot.getCoordinates().get(1));
            for (Ship actual : game.getShips()) {
                for (int i = 0; i < actual.getCoordinates().size(); i++) {
                    if ((actual.getCoordinates().get(i).getX() == x) && (actual.getCoordinates().get(i).getY() == y)) {
                        return true;
                    }
                }
            }
            return false;
        }

    public boolean isGeneratedShotAlreadyBeenShot(ShotCoordinate generatedShot, Long userId) {
        Game game = userGame.get(userId);
        return game.getAlreadyGeneratedShots().contains(generatedShot);
    }

    public Map<Long, Game> getUserGame() {
        return userGame;
    }

        public double winLossRation(int win, int loss){
            if(loss == 0){
                return win;
            } else {
                return ((double) win) /loss;
            }
        }


}

