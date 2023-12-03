package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Board;
import hu.progmatic.battleship_torpedotigrisek.model.Game;
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
    private final ShipPlacementService shipPlacementService;
@Autowired
    public GameController(ShipPlacementService shipPlacementService, GameService gameService) {
        this.shipPlacementService = shipPlacementService;
        this.gameService = gameService;
    }

    @GetMapping("/dinamicboard")
    public String getDynamicBoard() {
        return "dinamichtml1";
    }

    @GetMapping("/testBoard")
    public String gameBoard(Model model, GameService gameService) {
        Long userId = gameService.getCurrentUserId();
        System.out.println(gameService.getCurrentUserId());
        if (userId != null) {
            Game game = gameService.getUserGame().get(userId);
            if (game != null) {
                model.addAttribute("playerBoard", game.getPlayerBoard());
                model.addAttribute("enemyBoard", game.getEnemyBoard());
                return "test-board";
            }
        }
        // Ha nincs játék vagy a felhasználó nincs bejelentkezve, irányítsd át őket egy hibaoldalra vagy a főoldalra
        return "redirect:/some-error-page";
    }


}