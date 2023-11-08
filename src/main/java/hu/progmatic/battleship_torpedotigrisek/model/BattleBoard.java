package hu.progmatic.battleship_torpedotigrisek.model;

public class BattleBoard {

    private String [][] cells;

    public BattleBoard(){
        cells = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cells[i][j] = "_";

            }

        }

    }


}
