package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.CrossroadScene;
import main.java.scene.Direction;

public class HighlandMeadowFork extends CrossroadScene {

    protected HighlandMeadowFork(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.SOUTH_EAST, "Gopher Connector",GopherConnector::new);
        exits.addExit(Direction.NORTH_EAST, "Meadow-Trace", MeadowTrace::new);
        exits.addExit(Direction.NORTH_WEST, "Highland Vector", HighlandVector::new);

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Highland-Meadow Fork";
    }
}
