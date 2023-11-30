package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameService {
    private Game game;
    private ShipPlacementService shipPlacementService;

    public GameService(Game game, ShipPlacementService shipPlacementService) {
        this.game = game;
        this.shipPlacementService = shipPlacementService;
        newGame();
    }

    public Game getGame(){
        return this.game;
    }

    public void newGame(){
        game.setPlayerBoard(new Board());
        game.setEnemyBoard(new Board());
        game.setPlayerScore(0);
        game.setEnemyScore(0);
        game.setShipTypes(new ShipType[]{ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER});
        initializeEnemyShips();
        game.setShips(new ArrayList<>());
        game.setRemainingShips(new ArrayList<>(Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER,ShipType.ATTACKER)));
    }

    public void initializeEnemyShips() {
        List<Ship> enemyShips = generateShips();
        for (Ship ship : enemyShips) {
            shipPlacementService.placeShipRandomly(game.getEnemyBoard(), ship);
        }
    }

    public List<Ship> generateShips() {
        List<Ship> ships = new ArrayList<>();
        for (ShipType type : game.getShipTypes()) {
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL
            ships.add(new Ship(type, orientation));
        }
        return ships;
    }

    public void placeAllShips() {
        // Hajókat újra lerakjuk a listából
        for (ShipType shipType : game.getRemainingShips()) {
            Ship ship = new Ship(shipType, Math.random() < 0.5);
            if (shipPlacementService.placeShipRandomly(game.getPlayerBoard(), ship)) {
                game.getShips().add(ship);
            }
        }
    }

    public void resetGame() {
        // Hajólista törlése
        game.getShips().clear();

        // Tábla törlése
        shipPlacementService.clearShips(game.getPlayerBoard());
        shipPlacementService.clearShips(game.getEnemyBoard());

        resetRemainingShips();
    }

    public void resetRemainingShips() {
        game.setRemainingShips(Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER
        ));
    }

    public String whoIsTheWinner() {
        if (game.getPlayerScore() == 20) {
            return "You won";
        } else if (game.getEnemyScore() == 20) {
            return "You lose";
        }
        return null;
    }

    public boolean isGameFinished() {
        return game.getPlayerScore() == 20 || game.getEnemyScore() == 20;
    }
    public boolean isEnd(){ //ezt is lehet de az isGameFinished-et is lehet használni
        return game.isEnd();
    }

//  public boolean evaluateShot(ShotCoordinate shotCoordinate) {
//      shipPlacementService.
//  }
}
