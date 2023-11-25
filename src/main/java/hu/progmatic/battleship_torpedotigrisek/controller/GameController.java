package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Coordinate;
import hu.progmatic.battleship_torpedotigrisek.model.EnemyShip;
import hu.progmatic.battleship_torpedotigrisek.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GameController {
    private final Board playerBoard = new Board();
    private final Board enemyBoard = new Board();
    private final ShipService shipService;

    @Autowired
    public GameController(ShipService shipService) {
        this.shipService = shipService;
        initializeEnemyShips();
    }

    private void initializeEnemyShips() {
        List<EnemyShip> enemyShips = shipService.initializeEnemyShips();
        enemyBoard.placeEnemyShipsRandomly(enemyShips);
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
