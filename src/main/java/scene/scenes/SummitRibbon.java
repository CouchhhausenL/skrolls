package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class SummitRibbon extends PathScene {
    protected SummitRibbon(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.EAST, "Gopher-Summit-Rowan Cross", GopherSummitRowanCross::new);
        exits.addExit(Direction.WEST, "Peakwatch Tower", PeakwatchTower::new);
    }

    @Override
    public String getName() {
        return "Summit Ribbon";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
