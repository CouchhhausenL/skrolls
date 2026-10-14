package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class SaplingFord extends SettlementScene {

    protected SaplingFord(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_EAST, "Winterfort",Winterfort::new);
        exits.addExit(Direction.SOUTH_WEST, "Acorn Lane",AcornLane::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

    @Override
    public String getName() {
        return "Sapling Ford";
    }
}
