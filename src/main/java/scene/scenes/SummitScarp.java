package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class SummitScarp extends PathScene {
    protected SummitScarp(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Digeterel Summit Base", DigeterelSummitBase::new);
        exits.addExit(Direction.EAST, "Peakwatch Tower", PeakwatchTower::new);
    }

    @Override
    public String getName() {
        return "Summit Scarp";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
