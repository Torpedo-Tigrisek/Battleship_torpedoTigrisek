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
public class ShotCoordinate {
    private List<String> coordinates;

    @Override
    public String toString() {
        return "ShotCoordinate{" +
                "coordinates=" + coordinates +
                '}';
    }
}
