package hu.progmatic.battleship_torpedotigrisek.controller;


import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    @GetMapping("/testBoard")
    public String index(Model model) {
        Board myBoard = new Board();
        Board opponentBoard = new Board();

        Ship myShip = new Ship(4, 0, 0); // 4 hosszú hajó, elhelyezve (0,0) koordinátától kezdődően
        myBoard.placeShip(myShip);

        model.addAttribute("myGrid", myBoard.getGrid());
        model.addAttribute("opponentGrid", opponentBoard.getGrid());

        return "test-board";
    }
}
