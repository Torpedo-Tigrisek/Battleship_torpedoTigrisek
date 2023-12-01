package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import hu.progmatic.battleship_torpedotigrisek.model.ShipType;
import hu.progmatic.battleship_torpedotigrisek.service.ShipPlacementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {
    private final Board playerBoard = new Board();
    private final Board enemyBoard = new Board();
    private final ShipPlacementService shipPlacementService;


    private ShipType[] shipTypes = {ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER};

    public GameController(ShipPlacementService shipPlacementService) {
        this.shipPlacementService = shipPlacementService;


        initializeEnemyShips();
    }

    private void initializeEnemyShips() {
        List<Ship> enemyShips = generateShips();
        for (Ship ship : enemyShips) {
            shipPlacementService.placeShipRandomly(enemyBoard, ship);
         }
    }

    private List<Ship> generateShips() {
        List<Ship> ships = new ArrayList<>();
        for (ShipType type : shipTypes) {
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL
            ships.add(new Ship(type, orientation));
        }
        return ships;
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