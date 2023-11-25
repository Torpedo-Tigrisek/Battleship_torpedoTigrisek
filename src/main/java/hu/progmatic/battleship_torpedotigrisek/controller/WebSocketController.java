package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import hu.progmatic.battleship_torpedotigrisek.service.ShotService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class WebSocketController {

    private final Board playerBoard;
    private List<Ship> ships;
    private ShotService shotService;


    public WebSocketController(Board playerBoard, ShotService shotService) {
        this.playerBoard = playerBoard;
        this.ships = new ArrayList<>();
        this.shotService = shotService;
    }

    @MessageMapping("/placeShip")
    @SendTo("/topic/shipPlaced")
    public Board handleShipPlacement(Ship placement) {

        Ship newShip = new Ship(placement.getShipType(), placement.getStartX(), placement.getStartY(), placement.getOrientation());

        playerBoard.placeShip(newShip);
        ships.add(newShip);
        System.out.println(ships);
        // Frissítjük a tábla állapotát és visszaküldjük a frissített táblát a kliensnek
        return playerBoard;
    }
    @MessageMapping("/updateCell")
    @SendTo("/topic/boardUpdate")
    public Board updateCell(CellUpdateRequest request) {
        int rowIndex = request.getRowIndex();
        int colIndex = request.getColIndex();
        String newValue = request.getNewValue();

        // Feltételezve, hogy van egy olyan metódusod, ami frissíti a táblát és ellenőrzi az érvényességet
        boolean success = playerBoard.updateCell(rowIndex, colIndex, newValue);

        if (success) {
            // Ha a frissítés sikerült, küldd vissza a frissített tábla állapotát
            return playerBoard;
        } else {
            // Ha a frissítés nem sikerült (pl. érvénytelen koordináták vagy érték),
            // kezeld le a hibát megfelelően (pl. küldj vissza hibaüzenetet)
            // Itt egy egyszerű példa, de valóságban jobb lenne valamilyen hibaobjektumot küldeni
            return null;
        }
    }


    @MessageMapping("battle.sendShot")
    @SendTo("/topic/public")
    public ShotCoordinate sendShot(@Payload ShotCoordinate shotCoordinate) {
        System.out.println(shotCoordinate.getCoordinates());
        return shotCoordinate;
    }
    @SubscribeMapping("/generatedShot")
    public ShotCoordinate sendGeneratedShot() throws Exception {
        ShotCoordinate generatedShot = shotService.randomGeneratedShot();
        System.out.println("generatedShot = " + generatedShot.toString());
        return generatedShot;
    }
    @MessageMapping("battle.sendHit")
    @SendTo("/topic/public")
    public HitCoordinate sendHit(@Payload HitCoordinate hitCoordinate) {
        System.out.println(hitCoordinate.getHitCoordinates());
        return hitCoordinate;
    }

}