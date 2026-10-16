package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.LocationScene;

public class TheYggdrasoul extends LocationScene {
    protected TheYggdrasoul(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Exowerin", "Go back to Exowerin", Exowerin::new);
    }

    @Override
    public String getName() {
        return "The Yggdrasoul";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }

}
