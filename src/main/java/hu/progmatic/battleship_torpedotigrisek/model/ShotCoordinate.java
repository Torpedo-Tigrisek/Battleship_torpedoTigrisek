package hu.progmatic.battleship_torpedotigrisek.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShotCoordinate {
    private List<String> coordinates;
}
