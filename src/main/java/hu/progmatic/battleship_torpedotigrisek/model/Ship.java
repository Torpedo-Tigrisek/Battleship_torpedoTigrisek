package hu.progmatic.battleship_torpedotigrisek.model;

public class Ship {

    private int size;
    private int startX;
    private int startY;

    // Constructors, getters, and setters
    public Ship(int size, int startX, int startY) {
        this.size = size;
        this.startX = startX;
        this.startY = startY;
    }

    // Getterek és setterek
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getStartX() { return startX; }
    public void setStartX(int startX) { this.startX = startX; }
    public int getStartY() { return startY; }
    public void setStartY(int startY) { this.startY = startY; }
}
