package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class Clioppexar extends LocationScene {
    protected Clioppexar(GameContext gameContext) {
        super(gameContext);
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
