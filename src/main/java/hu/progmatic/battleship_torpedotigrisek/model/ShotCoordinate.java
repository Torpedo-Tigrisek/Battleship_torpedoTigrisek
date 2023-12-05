package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShotCoordinate {
    private List<String> coordinates;

    public boolean equals(Object shotCoordinate) {
        if (this == shotCoordinate) {
            return true;
        }

        if (shotCoordinate == null || getClass() != shotCoordinate.getClass()) {
            return false;
        }

        ShotCoordinate otherCoordinate = (ShotCoordinate) shotCoordinate;

        return Objects.equals(this.coordinates.get(0), otherCoordinate.coordinates.get(0)) &&
                Objects.equals(this.coordinates.get(1), otherCoordinate.coordinates.get(1));
    }


    @Override
    public String toString() {
        return "ShotCoordinate{" +
                "coordinates=" + coordinates +
                '}';
    }
}
