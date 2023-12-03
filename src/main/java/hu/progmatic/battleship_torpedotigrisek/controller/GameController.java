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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String gameBoard(Model model) {
        Long userId = gameService.getCurrentUserId();
        System.out.println("Accessing /testBoard with userId: " + userId);
        if (userId != null) {
            System.out.println("Looking up game for user ID: " + userId);
            Game game = gameService.getUserGame().get(userId);
            System.out.println("Game found: " + game);
            if (game != null) {
                model.addAttribute("playerBoard", game.getPlayerBoard());
                model.addAttribute("enemyBoard", game.getEnemyBoard());
                return "test-board";
            }
        }
        return "redirect:/some-error-page";
    }

    @GetMapping("/startGame")
    public String startGame(RedirectAttributes redirectAttributes) {
        Long userId = gameService.getCurrentUserId();
        if (userId != null) {
            gameService.startNewGameForUser(userId);
            gameService.initializeEnemyShips(userId);
            return "redirect:/testBoard";
        }
        redirectAttributes.addFlashAttribute("error", "Nem sikerült elindítani a játékot.");
        return "redirect:/home";
    }


}