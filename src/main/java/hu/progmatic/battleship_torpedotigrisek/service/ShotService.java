package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
@Service
public class ShotService {
    private GameService gameService;

    public ShotCoordinate randomGeneratedShot(){
        ShotCoordinate shot = new ShotCoordinate();
        Random randomGenerator = new Random();
        List<String> shotArray = new ArrayList<>();
        String x = String.valueOf(randomGenerator.nextInt(0,10));
        String y = String.valueOf(randomGenerator.nextInt(0,10));
        shotArray.add(x);
        shotArray.add(y);
        shot.setCoordinates(shotArray);
        return shot;
    }

 //   public ShotCoordinate intelligentShot(ShotCoordinate lastHitShot){
 //       ShotCoordinate intelligentShot = new ShotCoordinate();
 //       Map<ShipType, EnemyShip> ships = gameService.getGame().getEnemyBoard().getShipMap();
 //       for (var actual : ships.values()) {
 //           for (int i = 0; i < ships.size(); i++) {
 //               actual.getCoordinates().get(i)
 //           }
 //       }
 //       if(lastHitShot.getCoordinates().get(0) == ships.get())
 //       return intelligentShot;
 //   }
}
