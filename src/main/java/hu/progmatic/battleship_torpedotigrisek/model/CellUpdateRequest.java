package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.Data;

@Data
public class CellUpdateRequest {

    private int rowIndex;
    private int colIndex;
    private String newValue;
}
