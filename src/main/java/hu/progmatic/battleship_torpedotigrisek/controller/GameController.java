package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Ship;
import hu.progmatic.battleship_torpedotigrisek.model.ShipType;
import hu.progmatic.battleship_torpedotigrisek.service.GameService;
import hu.progmatic.battleship_torpedotigrisek.service.ShipPlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {
    private GameService gameService;

@Autowired
    public GameController( GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/testBoard")
    public String gameBoard(Model model) {
        model.addAttribute("playerBoard", gameService.getGame().getPlayerBoard());
        model.addAttribute("enemyBoard", gameService.getGame().getEnemyBoard());
        return "test-board";
    }
}