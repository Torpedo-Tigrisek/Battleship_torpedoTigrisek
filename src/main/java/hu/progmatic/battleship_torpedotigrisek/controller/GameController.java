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


    @GetMapping("/dinamicboard")
    public String getDinamycBoard(){
        return "dinamichtml1";
    }

    @GetMapping("/testBoard")
    public String gameBoard(Model model) {

        model.addAttribute("playerBoard", playerBoard);

        return "test-board";
    }



}
