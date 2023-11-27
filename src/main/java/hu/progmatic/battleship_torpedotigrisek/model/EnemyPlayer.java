package hu.progmatic.battleship_torpedotigrisek.model;

import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class EnemyPlayer {

    private EnemyPlayer enemyPlayer;
    private String name = "Computer";
    private Set<String> firedCoordinates = new HashSet<>();
    private boolean lastMoveHit = false;
    private int lastHitRow;
    private int lastHitCol;

    public EnemyPlayer(String name, EnemyPlayer enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    public void makeMove(List <String>hitCoordinates) {
        Random random = new Random();

        lastHitRow= Integer.parseInt(hitCoordinates.get(0));
        lastHitCol=Integer.parseInt(hitCoordinates.get(1));

        if (hitCoordinates.contains(lastMoveHit)) {
            // Ha az előző lépés talált, akkor véletlenszerűen válaszd meg a következő célpontot körülötte
            int row = random.nextInt(3) - 1 + lastHitRow; // -1, 0, vagy 1 hozzáadása a talált sorhoz
            int col = random.nextInt(3) - 1 + lastHitCol; // -1, 0, vagy 1 hozzáadása a talált oszlophoz

            // Ellenőrizd, hogy a célmezőn már lőttek-e, és hogy a koordináta a tábla határain belül van-e
            while (hasAlreadyFired(row, col) || row < 0 || row >= 10 || col < 0 || col >= 10) {
                // Ha a választott koordináta már lőtt vagy kilép a tábla határain, válassz új koordinátát
                row = random.nextInt(3) - 1 + lastHitRow;
                col = random.nextInt(3) - 1 + lastHitCol;
            }

            // Jelöld meg a célpontot lőttként
            markAsFired(row, col);

            // Ellenőrizd, hogy a lőtt lövéssel talált-e el valamit a játékos
            lastMoveHit = enemyPlayer.receiveAttack(row, col);

            // Ha talált, frissítsd a talált koordinátákat
            if (lastMoveHit) {
                lastHitRow = row;
                lastHitCol = col;
            }

            // Kiírhatod a lépést vagy bármilyen egyéb információt
            System.out.println(name + " shoots at " + row + "," + col + " and " + (lastMoveHit ? "hits!" : "misses."));
        } else {
            // Ha az előző lépés nem talált, úgy mint a jelenlegi implementáció
            int row = random.nextInt(10);
            int col = random.nextInt(10);

            // Ellenőrizd, hogy a célmezőn már lőttek-e
            while (hasAlreadyFired(row, col)) {
                row = random.nextInt(10);
                col = random.nextInt(10);
            }

            // Jelöld meg a célpontot lőttként
            markAsFired(row, col);

            // Ellenőrizd, hogy a lőtt lövéssel talált-e el valamit a játékos
            lastMoveHit = enemyPlayer.receiveAttack(row, col);

            // Ha talált, frissítsd a talált koordinátákat
            if (lastMoveHit) {
                lastHitRow = row;
                lastHitCol = col;
            }

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
