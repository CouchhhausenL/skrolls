package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Falconedge extends SettlementScene {

    protected Falconedge(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.SOUTH_EAST, "Highland Vector", HighlandVector::new);
        exits.addExit(Direction.WEST, "Highland Boulevard", HighlandBoulevard::new);
        exits.addExit("FalconedgeFortress", "Enter Falconedge Fortress", FalconedgeFortress::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Falconedge";
    }
}
