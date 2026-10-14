package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class ShaucroftHollows extends SettlementScene {

    protected ShaucroftHollows(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.SOUTH_WEST, "Meadow Trace",MeadowTrace::new);
        exits.addExit(Direction.EAST, "Meadow Trodden Path", MeadowTroddenPath::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
            return "Shaucroft Hollows";
    }
}
