package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.LocationScene;

import java.util.List;

public class DigeterelSummit extends LocationScene {
    private final Chest chest;

    public DigeterelSummit(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("digeterel_summit_chest_1", "Summit Altar Chest", Quality.UNCOMMON);
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Digeterel Summit Base", "Return to Digeterel Summit Base", DigeterelSummitBase::new);
    }

    @Override
    public String getName() {
        return "Digeterel Summit";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }
}
