package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class GameService {
    private Game game;
    private ShipPlacementService shipPlacementService;

    public GameService(Game game, ShipPlacementService shipPlacementService) {
        this.game = game;
        this.shipPlacementService = shipPlacementService;
        newGame();
    }

    public Game getGame() {
        return this.game;
    }

    public void newGame() {
        game.setPlayerBoard(new Board());
        game.setEnemyBoard(new Board());
        game.setPlayerScore(0);
        game.setEnemyScore(0);
        game.setShipTypes(new ShipType[]{ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER});
        initializeEnemyShips();
        game.setShips(new ArrayList<>());
        game.setEnemyShips(game.getEnemyShips());
        game.setRemainingShips(new ArrayList<>(Arrays.asList(
                ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER)));
        game.setAlreadyGeneratedShots(new ArrayList<>());
    }

    public void initializeEnemyShips() {
        List<Ship> enemyShips = generateShips();
        getGame().setEnemyShips(new ArrayList<>());
        for (Ship ship : enemyShips) {
            if(shipPlacementService.placeShipRandomly(game.getEnemyBoard(), ship)){
                getGame().getEnemyShips().add(ship);
            }

        }
        System.out.println("Enemy ships:" + game.getEnemyShips());

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

    public void fixShipPositions(List<Ship> shipsOnBoard, List<Ship> enemyShipsOnBoard) {
        shipPlacementService.fixShipPosition(shipsOnBoard, enemyShipsOnBoard);
    }

    public String whoIsTheWinner() {
        if (game.getPlayerScore() == 20) {
            return "You win";
        } else if (game.getEnemyScore() == 20) {
            return "You lose";
        }
        return null;
    }


    public boolean evaluatePlayerShot(ShotCoordinate shotCoordinate) {
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

    public ShotCoordinate randomGeneratedShot() {
        ShotCoordinate shot = new ShotCoordinate();
        Random randomGenerator = new Random();
        do{
            List<String> shotArray = new ArrayList<>();
            String x = String.valueOf(randomGenerator.nextInt(0, 10));
            String y = String.valueOf(randomGenerator.nextInt(0, 10));
            shotArray.add(x);
            shotArray.add(y);
            shot.setCoordinates(shotArray);
        }while(isGeneratedShotAlreadyBeenShot(shot));
        game.getAlreadyGeneratedShots().add(shot);
        return shot;
    }


    public boolean evaluateGeneratedShot(ShotCoordinate generatedShot) {
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

    public boolean isGeneratedShotAlreadyBeenShot(ShotCoordinate generatedShot) {
        return game.getAlreadyGeneratedShots().contains(generatedShot);
    }
}
