package hu.progmatic.battleship_torpedotigrisek.controller;


import hu.progmatic.battleship_torpedotigrisek.model.EnemyShip;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// (Bubu)
@RestController
public class ShipController {
    @GetMapping("/getEnemyShipsMap")
    public EnemyShip getEnemyShipsMap(){

        EnemyShip my_ship = EnemyShip.getEnemyShipById(2);
        return my_ship;
    }
}