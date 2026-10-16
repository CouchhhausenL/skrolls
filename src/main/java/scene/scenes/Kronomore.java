package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Kronomore extends SettlementScene {

    protected Kronomore(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.WEST, "Cathedral Lane", CathedralLane::new);
        exits.addExit(Direction.EAST, "Celestial Rill", CelestialRill::new);
        exits.addExit("ColdwallCastle", "Enter Coldwall Castle", ColdwallCastle::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

    @Override
    public String getName() {
        return "Kronomore";
    }
}
