package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class WinterLane extends PathScene {
    protected WinterLane(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Winterfort", Winterfort::new);
        exits.addExit(Direction.SOUTH_EAST, "The Chilly Cliff Pub", TheChillyCliffPub::new);
    }

    @Override
    public String getName() {
        return "Winter Lane";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
