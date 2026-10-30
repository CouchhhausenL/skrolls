package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

import java.util.List;

public class Clioppexar extends LocationScene {
    private final Chest chest;

    public Clioppexar(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("clioppexar_chest_1", "Clioppexar Ruin Chest", Quality.RARE);
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH_WEST, "Saphire Way", SaphireWay::new);
        exits.addExit(Direction.WEST, "Meadow Trodden Path", MeadowTroddenPath::new);
    }

    @Override
    public String getName() {
        return "Clioppexar";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
