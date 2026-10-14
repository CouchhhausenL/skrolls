package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Musterfield extends SettlementScene {

    protected Musterfield(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.SOUTH_WEST, "Rowan Avenue",RowanAvenue::new);
        exits.addExit(Direction.EAST, "Rowan Connector",RowanConnector::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Musterfield";
    }
}
