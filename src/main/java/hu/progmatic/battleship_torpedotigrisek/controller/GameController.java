package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.EnemyShip;
import hu.progmatic.battleship_torpedotigrisek.model.ShipType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {
    private final Board playerBoard = new Board();
    private final Board enemyBoard = new Board();
    private ShipType[] shipTypes = {ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER};

    public GameController() {
        initializeEnemyShips();
    }

    private void initializeEnemyShips() {
        List<EnemyShip> enemyShips = generateEnemyShips();
        enemyBoard.placeEnemyShipsRandomly(enemyShips);
    }

    private List<EnemyShip> generateEnemyShips() {
        List<EnemyShip> enemyShips = new ArrayList<>();
        for (ShipType type : shipTypes) {
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL
            EnemyShip enemyShip = new EnemyShip(type, type.getSize(), orientation);
            enemyShips.add(enemyShip);
        }
        return enemyShips;
    }

    @GetMapping("/dinamicboard")
    public String getDynamicBoard() {
        return "dinamichtml1";
    }

    @GetMapping("/testBoard")
    public String gameBoard(Model model) {
        model.addAttribute("playerBoard", playerBoard);
        model.addAttribute("enemyBoard", enemyBoard);
        return "test-board";
    }
}