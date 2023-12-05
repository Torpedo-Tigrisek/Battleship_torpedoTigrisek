package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private Board playerBoard;
    private Board enemyBoard;
    private int playerScore;
    private int enemyScore;
    private ShipType[] shipTypes;
    private List<Ship> ships;
    private List<Ship> enemyShips;
    private List<ShipType> remainingShips;
    private List<ShotCoordinate> alreadyGeneratedShots;
}
