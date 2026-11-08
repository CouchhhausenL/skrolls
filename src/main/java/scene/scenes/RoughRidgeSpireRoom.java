package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.LocationScene;

import java.util.List;

public class RoughRidgeSpireRoom extends LocationScene {
    private final Chest chest;

    public RoughRidgeSpireRoom(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("rough_ridge_chest_1", "Windswept Spire Chest", Quality.RARE);
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Leave Rough Ridge Spire", "Leave Rough Ridge Spire", RoughRidgeSpire::new);

    }

    @Override
    public String getName() {
        return "Rough Ridge Spire Room";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
