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

    public Boolean SaveEnemyShip() {
        // (BUBU) Save to DB (insert)
        return true;
    }

    public static EnemyShip getEnemyShipById(Integer id) {
        // (BUBU) Get from DB (select * from ships where id = ? VALUES)
        return new EnemyShip(ShipType.ATTACKER, 3, true);
    }

    public void addCoordinate(int x, int y) {
        coordinates.add(new Coordinate(x, y));
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }
    // Az inicializáció logikája itt történik a méret és orientáció alapján
}