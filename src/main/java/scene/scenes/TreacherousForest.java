package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.LocationScene;

import java.util.List;

public class TreacherousForest extends LocationScene {
    private final Chest chest;

    public TreacherousForest(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("treacherous_forest_chest_1", "Overgrown Bramble Chest", Quality.RARE);
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Ashwick", "Leave Treacherous Forest", Ashwick::new);
    }

    @Override
    public String getName() {
        return "Treacherous Forest";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
