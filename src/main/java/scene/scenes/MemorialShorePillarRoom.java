package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.LocationScene;

import java.util.List;

public class MemorialShorePillarRoom extends LocationScene {
    private final Chest chest;


    public MemorialShorePillarRoom(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("memorial_pillar_chest_1", "Memorial Shore Urn", Quality.LEGENDARY);
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Leave Memorial Shore Pillar", "Leave Memorial Shore Pillar", MemorialShorePillar::new);
    }

    @Override
    public String getName() {
        return "Memorial Shore Pillar Room";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }
}
