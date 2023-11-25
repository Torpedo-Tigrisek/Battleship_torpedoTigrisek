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
public class HitCoordinate {
    private List<String> hitCoordinates;

    @Override
    public String toString() {
        return "HitCoordinate{" +
                "hitCoordinates=" + hitCoordinates +
                '}';
    }
}
