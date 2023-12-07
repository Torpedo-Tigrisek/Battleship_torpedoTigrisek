package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.Game;
import hu.progmatic.battleship_torpedotigrisek.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class GameController {
    private GameService gameService;

@Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @GetMapping("/testBoard")
    public String gameBoard(Model model) {
        Long userId = gameService.getCurrentUserId();
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


            return "redirect:/testBoard";
        }
        redirectAttributes.addFlashAttribute("error", "Nem sikerült elindítani a játékot.");
        return "redirect:/home";
    }
}