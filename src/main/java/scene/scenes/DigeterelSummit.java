package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.LocationScene;

public class DigeterelSummit extends LocationScene {
    protected DigeterelSummit(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Digeterel Summit Base", "Return to Digeterel Summit Base", DigeterelSummitBase::new);
    }

    @Override
    public String getName() {
        return "Digeterel Summit";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
