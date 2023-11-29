package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.EnemyShip;
import hu.progmatic.battleship_torpedotigrisek.model.Game;
import hu.progmatic.battleship_torpedotigrisek.model.ShipType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private Game game;

    public GameService(Game game) {
        this.game = game;
        newGame();
    }

    public void newGame(){
        game.setPlayerBoard(new Board());
        game.setEnemyBoard(new Board());
        game.setPlayerScore(0);
        game.setEnemyScore(0);
        game.setShipTypes(new ShipType[]{ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE,
                ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.ATTACKER,
                ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER});
        initializeEnemyShips();
        game.setShips(new ArrayList<>());
    }

    public Game getGame(){
        return this.game;
    }
    private void initializeEnemyShips() {
        List<EnemyShip> enemyShips = generateEnemyShips();
        game.getEnemyBoard().placeEnemyShipsRandomly(enemyShips);
    }

    private List<EnemyShip> generateEnemyShips() {
        List<EnemyShip> enemyShips = new ArrayList<>();
        for (ShipType type : game.getShipTypes()) {
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL
            EnemyShip enemyShip = new EnemyShip(type, type.getSize(), orientation);
            enemyShips.add(enemyShip);
        }
        return enemyShips;
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
}
