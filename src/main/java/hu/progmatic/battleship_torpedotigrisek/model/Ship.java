package hu.progmatic.battleship_torpedotigrisek.model;




public class Ship {

    public enum ShipNameList {
        CARRIER, CRUISER, SUBMARINE, DESTROYER, ATTACKER
    }

    private ShipNameList shipName;
    private int size;
    private int[] coordinate;
    private int countHit;


    public Ship (int size, ShipNameList shipName) {
        this.size = size;
        this.countHit = 0;
        coordinate = new int[size];
        for (int i = 0; i<size; i++) {
            coordinate[i] = 0;
        }
        this.shipName = shipName;
    }

    public ShipNameList getShipName() {
        return shipName;
    }

    public void setShipName(ShipNameList shipName) {
        this.shipName = shipName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int[] coordinate) {
        this.coordinate = coordinate;
    }

    public int getCountHit() {
        return countHit;
    }

    public void setCountHit(int countHit) {
        this.countHit = countHit;
    }
}
