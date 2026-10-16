package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.LocationScene;

public class SoulObelisk extends LocationScene {
    protected SoulObelisk(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Ashwick", "Leave Soul Obelisk", Ashwick::new);
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
