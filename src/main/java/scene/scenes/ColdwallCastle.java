package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.LocationScene;

public class ColdwallCastle extends LocationScene {
    protected ColdwallCastle(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Kronomore", "Leave Castle, back to City", Kronomore::new);
    }

    @Override
    public String getName() {
        return "Coldwall Castle";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
