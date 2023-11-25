package hu.progmatic.battleship_torpedotigrisek.model;

import java.util.ArrayList;
import java.util.List;

public class Fleet {


    private final List<Ship> fleet = new ArrayList<>();

    public Fleet() {
        fleet.add(new Ship(ShipType.CRUISER));
        fleet.add(new Ship(ShipType.SUBMARINE));
        fleet.add(new Ship(ShipType.SUBMARINE));
        fleet.add(new Ship(ShipType.DESTROYER));
        fleet.add(new Ship(ShipType.DESTROYER));
        fleet.add(new Ship(ShipType.DESTROYER));
        fleet.add(new Ship(ShipType.ATTACKER));
        fleet.add(new Ship(ShipType.ATTACKER));
        fleet.add(new Ship(ShipType.ATTACKER));
    }
}
