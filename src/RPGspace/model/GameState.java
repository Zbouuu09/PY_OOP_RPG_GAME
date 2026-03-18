package RPGspace.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private Player player;
    private int currentStage; // 1..3

    public GameState(Player player) {
        this.player = player;
        this.currentStage = 1;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void advanceStage() {
        currentStage++;
    }

    public boolean isGameCompleted() {
        return currentStage > 3;
    }
}