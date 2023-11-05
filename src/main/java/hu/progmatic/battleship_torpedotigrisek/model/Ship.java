package hu.progmatic.battleship_torpedotigrisek.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipId;
    private String shipType;
    @ElementCollection
    private List<String> shipLocation = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "player_id")
    private User player;

    public Ship(Long shipId, String shipType, List<String> shipLocation, User player) {
        this.shipId = shipId;
        this.shipType = shipType;
        this.shipLocation = shipLocation;
        this.player = player;
    }

    public Ship() {
    }

    public Long getShipId() {
        return shipId;
    }

    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getShipLocation() {
        return shipLocation;
    }

    public void setShipLocation(List<String> shipLocation) {
        this.shipLocation = shipLocation;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }
}
