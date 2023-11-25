package hu.progmatic.battleship_torpedotigrisek.model;

public enum ShipType {

    CRUISER(4),
    SUBMARINE(3),
    DESTROYER(2),
    ATTACKER(1);
    private final int size;

    ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}
