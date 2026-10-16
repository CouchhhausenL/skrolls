package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.LocationScene;

public class TreacherousForest extends LocationScene {
    protected TreacherousForest(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Ashwick", "Leave Treacherous Forest", Ashwick::new);
    }

    @Override
    public String getName() {
        return "Soul Obelisk";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
