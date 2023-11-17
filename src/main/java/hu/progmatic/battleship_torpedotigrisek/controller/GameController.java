package hu.progmatic.battleship_torpedotigrisek.controller;


import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import hu.progmatic.battleship_torpedotigrisek.model.ShipType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {
    private final Board playerBoard = new Board();
    private List<Ship> ships = new ArrayList<>();


    /*
    @GetMapping("/testBoard")
    public String getTestBoard(Model model) {
        Board myBoard = new Board();
        Board opponentBoard = new Board();

        Ship myShip = new Ship(0, 0, 0);
        myBoard.placeShip(myShip);

        model.addAttribute("myGrid", myBoard.getGrid());
        model.addAttribute("opponentGrid", opponentBoard.getGrid());

        return "test-board";
    }
    */


    @GetMapping("/dinamicboard")
    public String getDinamycBoard(){
        return "dinamichtml1";
    }

    @GetMapping("/testBoard")
    public String gameBoard(Model model) {

        model.addAttribute("playerBoard", playerBoard);

        return "test-board";
    }
/*
    @PostMapping("/placeShip")
    public String placeShip(Ship ship) {

        playerBoard.placeShip(ship);
        System.out.println("Location: " + ship);
        ships.add(ship);
        System.out.println(ships);

        return "redirect:/testBoard";
    }

 */

    /*
    @PostMapping("/placeShip")
    public String placeShip(@RequestParam ShipType shipType,
                            @RequestParam int startX,
                            @RequestParam int startY,
                            @RequestParam String orientation) {
        // Létrehoz egy új hajót a felhasználó által megadott paraméterekkel
        Ship newShip = new Ship(shipType, startX, startY, orientation);

        // Hozzáadja a hajót a táblához
        playerBoard.addShip(newShip);

        // Elhelyezi a hajót a táblán
        playerBoard.placeShip(newShip);
        ships.add(newShip);
        System.out.println("Location: " + newShip);
        System.out.println(ships);


        return "redirect:/testBoard";
    }

     */


}
