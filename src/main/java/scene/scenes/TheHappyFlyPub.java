package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class TheHappyFlyPub extends LocationScene {
    protected TheHappyFlyPub(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Gilded Nexus", GildedNexus::new);
        exits.addExit(Direction.WEST, "Gilded Approach", GildedApproach::new);
    }

    @Override
    public String getName() {
        return "The Happy Fly Pub";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
