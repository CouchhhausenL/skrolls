package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class TheChillyCliffPub extends LocationScene {


    public TheChillyCliffPub(GameContext gameContext) {
        super(gameContext);
    }


    @Override
    protected void setupExits() {
        exits.addExit(Direction.EAST, "Seaside Route", SeasideRoute::new);
        exits.addExit(Direction.NORTH_WEST, "WinterLane", WinterLane::new);
        exits.addExit(Direction.WEST, "Celestial Rill", CelestialRill::new);
        exits.addExit("Enter The Chilly Cliff Pub", "Enter the Chilly Cliff Pub", TheChillyCliffPubRoom::new);
    }

    @Override
    public String getName() {
        return "The Chilly Cliff Pub";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.INTERMEDIATE;
    }
}
