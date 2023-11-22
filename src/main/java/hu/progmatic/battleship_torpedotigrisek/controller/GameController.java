package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
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
    private List<Ship> ships = new ArrayList<>();
    private List<Ship> enemyShip = new ArrayList<>();

    @GetMapping("/dinamicboard")
    public String getDinamycBoard() {
        return "dinamichtml1";
    }

    @GetMapping("/testBoard")
    public String gameBoard(Model model) {
        model.addAttribute("playerBoard", playerBoard);

        Ship enemyShip1 = new Ship(ShipType.ATTACKER, 3, 2, "HORIZONTAL");
        Ship enemyShip2 = new Ship(ShipType.ATTACKER, 1, 5, "HORIZONTAL");
        Ship enemyShip3 = new Ship(ShipType.ATTACKER, 3, 6, "VERTICAL");
        Ship enemyShip4 = new Ship(ShipType.ATTACKER, 9, 9, "VERTICAL");
        Ship enemyShip5 = new Ship(ShipType.DESTROYER, 1, 1, "VERTICAL");
        Ship enemyShip6 = new Ship(ShipType.DESTROYER, 6, 1, "VERTICAL");
        Ship enemyShip7 = new Ship(ShipType.DESTROYER, 9, 4, "VERTICAL");
        Ship enemyShip8 = new Ship(ShipType.SUBMARINE, 4, 4, "HORIZONTAL");
        Ship enemyShip9 = new Ship(ShipType.SUBMARINE, 6, 6, "VERTICAL");
        Ship enemyShip10 = new Ship(ShipType.CRUISER, 1, 8, "HORIZONTAL");

        enemyBoard.placeShip(enemyShip1);
        enemyBoard.placeShip(enemyShip2);
        enemyBoard.placeShip(enemyShip3);
        enemyBoard.placeShip(enemyShip4);
        enemyBoard.placeShip(enemyShip5);
        enemyBoard.placeShip(enemyShip6);
        enemyBoard.placeShip(enemyShip7);
        enemyBoard.placeShip(enemyShip8);
        enemyBoard.placeShip(enemyShip9);
        enemyBoard.placeShip(enemyShip10);

        model.addAttribute("enemyBoard", enemyBoard);

        return "test-board";
    }
}