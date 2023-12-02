package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
    private boolean isEnd;
}
