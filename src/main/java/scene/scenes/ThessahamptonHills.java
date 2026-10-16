package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.LocationScene;

public class ThessahamptonHills extends LocationScene {
    protected ThessahamptonHills(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit("CathedralLane", "Return to CathedralLane", CathedralLane::new);
    }

    @Override
    public String getName() {
        return "Thessahampton Hills";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
