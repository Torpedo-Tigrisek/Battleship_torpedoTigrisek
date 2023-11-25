package hu.progmatic.battleship_torpedotigrisek.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship {

    private ShipType shipType;
    private int size;
    private boolean orientation;
    private List<Coordinate> coordinates;





    public Ship(ShipType shipType, boolean orientation) {
        this.shipType = shipType;
        this.size = shipType.getSize();
        this.orientation = orientation;
        this.coordinates = new ArrayList<>(this.size);

        for (int i = 0; i < size; i++) {
            coordinates.add(new Coordinate(0,0));

        }


    }

    public Ship(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setCoordinates(int startX, int startY, boolean isHorizontal) {
      //  this.coordinates.clear(); // Először töröljük a korábbi koordinátákat

        for (int i = 0; i < this.size; i++) {
            if (isHorizontal) {
                // Vízszintes elhelyezés: növeljük az X koordinátát
                this.coordinates.add(new Coordinate(startX + i, startY));
            } else {
                // Függőleges elhelyezés: növeljük az Y koordinátát
                this.coordinates.add(new Coordinate(startX, startY + i));
            }
        }
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public int getSize() {
        return shipType.getSize();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipType=" + shipType +
                ", size=" + size +
                ", orientation=" + orientation +
                ", coordinates=" + coordinates +
                '}';
    }
}
