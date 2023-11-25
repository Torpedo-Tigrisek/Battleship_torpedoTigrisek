package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
public class EnemyShip {
    private final ShipType shipType;
    private final int size;
    private boolean orientation;
    private List<Coordinate> coordinates;

    public EnemyShip(ShipType shipType, int size, boolean orientation) {
        this.shipType = shipType;
        this.size = size;
        this.orientation = orientation;
        this.coordinates = new ArrayList<>();
    }

    public void addCoordinate(int x, int y) {
        coordinates.add(new Coordinate(x, y));
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }
    // Az inicializáció logikája itt történik a méret és orientáció alapján
}