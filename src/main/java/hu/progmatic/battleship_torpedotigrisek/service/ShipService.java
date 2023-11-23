package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.EnemyShip;
import hu.progmatic.battleship_torpedotigrisek.model.ShipType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipService {

    public List<EnemyShip> initializeEnemyShips() {
        ShipType[] shipTypes = {ShipType.CRUISER, ShipType.SUBMARINE, ShipType.SUBMARINE, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER, ShipType.ATTACKER};

        List<EnemyShip> enemyShips = new ArrayList<>();
        for (ShipType type : shipTypes) {
            boolean orientation = Math.random() < 0.5; // true: HORIZONTAL, false: VERTICAL
            EnemyShip enemyShip = new EnemyShip(type, type.getSize(), orientation);
            enemyShips.add(enemyShip);
        }
        return enemyShips;
    }
}
