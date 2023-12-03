package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.*;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class GameService {
    private ShipPlacementService shipPlacementService;
    private Map<Long, Game> userGame = new HashMap<>();

    public GameService(ShipPlacementService shipPlacementService) {

        this.shipPlacementService = shipPlacementService;

    }

    public Game startNewGameForUser(Long userId) {

        if (userId != null) {
            Game game = newGame(userId);
            userGame.put(userId, game);
            return game;
        }
        return null;
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
        initializeEnemyShips(userId);
        game.setShips(new ArrayList<>());
        game.setEnemyShips(game.getEnemyShips());
        game.setRemainingShips(new ArrayList<>(Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER)));
        return game;
    }

    public void initializeEnemyShips(Long userId) {

        Game game = userGame.get(userId);
        if (game != null) {
            List<Ship> enemyShips = generateShips(userId);
            game.setEnemyShips(new ArrayList<>());
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

    public void placeAllShips(Long userId) {
        // Hajókat újra lerakjuk a listából

        Game game = userGame.get(userId);
        for (ShipType shipType : game.getRemainingShips()) {
            Ship ship = new Ship(shipType, Math.random() < 0.5);
            if (shipPlacementService.placeShipRandomly(game.getPlayerBoard(), ship)) {
                game.getShips().add(ship);
            }
        }

    }

    public void resetGame(Long userId) {

        Game game = userGame.get(userId);
        // Hajólista törlése
        game.getShips().clear();

        // Tábla törlése
        shipPlacementService.clearShips(game.getPlayerBoard());
        shipPlacementService.clearShips(game.getEnemyBoard());

        resetRemainingShips(userId);
    }

    public void resetRemainingShips(Long userId) {

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

    public String whoIsTheWinner(Long userId) {

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
        if (game != null && (game.getPlayerScore() == 20 || game.getEnemyScore() == 20)) {
            userGame.remove(userId);
            return true;
        }
        return false;
    }

    public boolean isEnd() { //ezt is lehet de az isGameFinished-et is lehet használni
        Long userId = getCurrentUserId();
        Game game = userGame.get(userId);
        return game.isEnd();
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


    public boolean evaluateGeneratedShot(ShotCoordinate generatedShot, Long userId) {

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

    public void updatePlayerScore(Long userId) {
        Game game = userGame.get(userId);
        if (game != null) {
            game.setPlayerScore(game.getPlayerScore() + 1);
        }
    }

    public void updateEnemyScore(Long userId) {
        Game game = userGame.get(userId);
        if (game != null) {
            game.setEnemyScore(game.getEnemyScore() + 1);
        }
    }
    public Map<Long, Game> getUserGame() {
        return userGame;
    }


}
