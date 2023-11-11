package hu.progmatic.battleship_torpedotigrisek.controller;


import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    @GetMapping("/testBoard")
    public String getTestBoard(Model model) {
        Board myBoard = new Board();
        Board opponentBoard = new Board();

        Ship myShip = new Ship(4, 0, 0);
        myBoard.placeShip(myShip);

        model.addAttribute("myGrid", myBoard.getGrid());
        model.addAttribute("opponentGrid", opponentBoard.getGrid());

        return "test-board";
    }

    @GetMapping("/dinamicboard")
    public String getDinamycBoard(){
        return "dinamichtml1";
    }
}
