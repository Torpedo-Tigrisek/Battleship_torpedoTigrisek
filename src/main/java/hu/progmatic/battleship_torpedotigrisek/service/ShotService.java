package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.ShotCoordinate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ShotService {

    public ShotCoordinate randomGeneratedShot(){
        ShotCoordinate shot = new ShotCoordinate();
        Random randomGenerator = new Random();
        List<String> shotArray = new ArrayList<>();
        String x = String.valueOf(randomGenerator.nextInt(0,11));
        String y = String.valueOf(randomGenerator.nextInt(0,11));
        shotArray.add(x);
        shotArray.add(y);
        shot.setCoordinates(shotArray);
        return shot;
    }
}
