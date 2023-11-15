package hu.progmatic.battleship_torpedotigrisek.model;

import java.util.List;

public class Ship {

    private int size;
    private int startX;
    private int startY;

    private List<Ship> ships;


    public Ship(int size, int startX, int startY, List<Ship> ships) {


        this.size = size;
        this.startX = startX;
        this.startY = startY;
        this.ships = ships;
    }


    public int getSize() { return size; }

    public void setSize(int size) { this.size = size; }
    public int getStartX() { return startX; }
    public void setStartX(int startX) { this.startX = startX; }
    public int getStartY() { return startY; }
    public void setStartY(int startY) { this.startY = startY; }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "size=" + size +
                ", startX=" + startX +
                ", startY=" + startY +
                '}';
    }
}
