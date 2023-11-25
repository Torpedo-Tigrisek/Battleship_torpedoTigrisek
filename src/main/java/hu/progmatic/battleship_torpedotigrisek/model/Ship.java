package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private ShipType shipType;
    private int size;
    private int startX;
    private int startY;
    private String orientation;




    public Ship(ShipType shipType, int startX, int startY, String orientation) {
        this.shipType = shipType;
        this.size = shipType.getSize();
        this.startX = startX;
        this.startY = startY;
        this.orientation = orientation;

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

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipType=" + shipType +
                ", size=" + size +
                ", startX=" + startX +
                ", startY=" + startY +
                ", orientation='" + orientation + '\'' +
                '}';
    }
}

