package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor

public class Ship {

    private ShipType shipType;
    private boolean orientation;
    private List<Coordinate> coordinates;


    public Ship(ShipType shipType, boolean orientation) {
        this.shipType = shipType;
        this.orientation = orientation;
        this.coordinates = new ArrayList<>();
        for (int i = 0; i < this.shipType.getSize(); i++) {
            coordinates.add(new Coordinate(0, 0)); // Kezdeti pozíciók, amik később frissítésre kerülnek
        }
    }

    public Ship() {
    }



}

