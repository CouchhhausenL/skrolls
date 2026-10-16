package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class PeakwatchTower extends LocationScene {
    protected PeakwatchTower(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.EAST, "Summit Ribbon", SummitRibbon::new);
        exits.addExit(Direction.WEST, "Summit Scarp", SummitScarp::new);
    }

    @Override
    public String getName() {
        return "Peakwatch Tower";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
