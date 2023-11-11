package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.PositionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GameController {

    @GetMapping("/game")
    public String gameBoard(Model model) {
        return "game";
    }

    @PostMapping("/saveXPositions")
    @ResponseBody
    public String saveXPositions(@RequestBody PositionRequest request) {
        // Itt kezeld az "X" pozíciókkal kapcsolatos adatokat
        List<String> xPositions = request.getXPositions();
        System.out.println("Received X positions on the server: " + xPositions);
        // A megfelelő logikát megvalósítod, például az adatok adatbázisba mentése
        // Visszaadhatod egy választ is, ha szükséges
        return "X positions received successfully";
    }
}
