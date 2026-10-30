package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.LocationScene;

import java.util.List;

public class ThessahamptonHills extends LocationScene {
    private final Chest chest;

    public ThessahamptonHills(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("thessahampton_chest_1", "Hillside Strongbox", Quality.RARE);
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest);
    }

    @Override
    protected void setupExits() {
        exits.addExit("CathedralLane", "Return to CathedralLane", CathedralLane::new);
    }

    @Override
    public String getName() {
        return "Thessahampton Hills";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
