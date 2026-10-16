package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class GildedApproach extends PathScene {
    protected GildedApproach(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "The Happy Fly Pub", TheHappyFlyPub::new);
        exits.addExit(Direction.SOUTH_EAST, "Winterfort", Winterfort::new);
    }

    @Override
    public String getName() {
        return "Gilded Approach";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
