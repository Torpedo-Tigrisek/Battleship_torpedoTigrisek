package hu.progmatic.battleship_torpedotigrisek.model;

import java.util.Random;

import java.util.HashSet;

import java.util.Set;

public class EnemyPlayer {

    private EnemyPlayer enemyPlayer;
    private String name="Computer";
    private Set<String> firedCoordinates = new HashSet<>();
    private boolean lastMoveHit = false;

    public EnemyPlayer( String name,EnemyPlayer enemyPlayer) {

        this.enemyPlayer=enemyPlayer;
    }

    public void makeMove() {
        Random random = new Random();

        if (lastMoveHit) {
            // Ha az előző lépés talált, akkor újra lépjen
            lastMoveHit = false;
        } else {
            int row = random.nextInt(10);
            int col = random.nextInt(10);

            // Ellenőrizd, hogy a célmezőn már lőttek-e
            while (hasAlreadyFired(row, col)) {
                row = random.nextInt(10);
                col = random.nextInt(10);
            }

            // Ellenőrizd, hogy a lőtt lövéssel talált-e el valamit a játékos
           // lastMoveHit =enemyPlayer.receiveAttack(row, col);

            // Jelöld meg a célpontot lőttként
            markAsFired(row, col);

            // Kiírhatod a lépést vagy bármilyen egyéb információt
            System.out.println(name + " shoots at " + row + "," + col + " and " + (lastMoveHit ? "hits!" : "misses."));
        }
    }

    private boolean hasAlreadyFired(int row, int col) {
        String coordinateKey = row + "," + col;
        return firedCoordinates.contains(coordinateKey);
    }

    private void markAsFired(int row, int col) {
        String coordinateKey = row + "," + col;
        firedCoordinates.add(coordinateKey);
    }

    public String getName() {
        return name;
    }
}