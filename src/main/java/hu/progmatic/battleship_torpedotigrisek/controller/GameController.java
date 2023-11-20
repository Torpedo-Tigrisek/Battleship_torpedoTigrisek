package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.ChatMessage;
import hu.progmatic.battleship_torpedotigrisek.model.PositionRequest;
import hu.progmatic.battleship_torpedotigrisek.model.ShotCoordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
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

    @MessageMapping("chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println(chatMessage.getContent());
        return chatMessage;
    }
    @MessageMapping("chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        // connection between user and the websocket
        // add username in websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("battle.sendShot")
    @SendTo("/topic/public")
    public ShotCoordinate sendShot(@Payload ShotCoordinate shotCoordinate){
        System.out.println(shotCoordinate.getCoordinates());
        return shotCoordinate;
    }

}
