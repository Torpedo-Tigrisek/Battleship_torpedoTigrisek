package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Data;

import java.util.List;
@Data
public class PositionRequest {
    private List<String> xPositions;
}