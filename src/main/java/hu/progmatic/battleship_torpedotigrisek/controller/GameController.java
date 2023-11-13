package hu.progmatic.battleship_torpedotigrisek.controller;


import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    private Board playerBoard = new Board();

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
        model.addAttribute("board", playerBoard);
        return "test-board";
    }

    @PostMapping("/placeShip")
    public String placeShip(Ship ship, Model model) {
        playerBoard.placeShip(ship);
        System.out.println("Hajó elhelyezve: " + ship);
        return "redirect:/testBoard";
    }
}
