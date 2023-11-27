package hu.progmatic.battleship_torpedotigrisek.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyPlayer {
    private List<String> shotsFired;  // Tárolja a már leadott lövések koordinátáit
    private boolean lastShotHit;      // Igaz, ha az előző lövés talált

    public EnemyPlayer() {
        this.shotsFired = new ArrayList<>();
        this.lastShotHit = false;
    }

    public String fireShot() {
        String coordinates;

        if (lastShotHit) {
            coordinates = generateRandomAroundLastHit();
        } else {
            coordinates = generateRandomNotFired();
        }

        shotsFired.add(coordinates);
        return coordinates;
    }

    private String generateRandomAroundLastHit() {
        Random rand = new Random();
        String lastShot = shotsFired.get(shotsFired.size() - 1);

        int row = Character.getNumericValue(lastShot.charAt(0));
        int col = Character.getNumericValue(lastShot.charAt(1));

        // Generálj random koordinátát a talált hely körül
        int newRow = row + rand.nextInt(3) - 1;
        int newCol = col + rand.nextInt(3) - 1;

        // Ellenőrizd, hogy a generált koordináta a táblán belül van-e
        newRow = Math.max(0, Math.min(9, newRow));
        newCol = Math.max(0, Math.min(9, newCol));

        return newRow + "" + newCol;
    }

    private String generateRandomNotFired() {
        Random rand = new Random();
        String coordinates;

        do {
            int row = rand.nextInt(10);
            int col = rand.nextInt(10);
            coordinates = row + "" + col;
        } while (shotsFired.contains(coordinates));

        return coordinates;
    }

    public void setLastShotHit(boolean lastShotHit) {
        this.lastShotHit = lastShotHit;
    }


}
