package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.*;

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

    public void setStartPosition(int startX, int startY) {
        coordinates.clear();
        for (int i = 0; i < shipType.getSize(); i++) {
            if (orientation) { // HORIZONTAL
                coordinates.add(new Coordinate(startX + i, startY));
            } else { // VERTICAL
                coordinates.add(new Coordinate(startX, startY + i));
            }
        }
    }


}

