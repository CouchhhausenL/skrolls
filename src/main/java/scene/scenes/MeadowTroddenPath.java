package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class MeadowTroddenPath extends PathScene {
    protected MeadowTroddenPath(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.EAST, "Clioppexar", Clioppexar::new);
        exits.addExit(Direction.WEST, "Shaucroft Hollows", ShaucroftHollows::new);
    }

    @Override
    public String getName() {
        return "Meadow Trodden Path";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
