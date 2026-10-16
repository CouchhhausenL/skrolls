package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.LocationScene;

public class FalconedgeFortress extends LocationScene {
    protected FalconedgeFortress(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Falconedge", "Leave Falconedge Fortress", Falconedge::new);
    }

    @Override
    public String getName() {
        return "Falconedge Fortress";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
