package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

import java.util.List;

public class RoughRidgeSpire extends LocationScene {
    private final Chest chest;

    public RoughRidgeSpire(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("rough_ridge_chest_1", "Windswept Spire Chest", Quality.RARE);
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH_EAST, "Granit Route", GranitRoute::new);
        exits.addExit(Direction.WEST, "Rowan Connector", RowanConnector::new);
        exits.addExit(Direction.NORTH_EAST, "Saphire Way", SaphireWay::new);
    }

    @Override
    public String getName() {
        return "Rough Ridge Spire";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
