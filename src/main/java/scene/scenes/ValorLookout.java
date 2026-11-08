package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class ValorLookout extends LocationScene {


    public ValorLookout(GameContext gameContext) {
        super(gameContext);


    }


    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Redwood Connector", RedwoodConnector::new);
        exits.addExit(Direction.EAST, "Cathedral Lane", CathedralLane::new);
        exits.addExit("Enter Valor Lookout", "Enter Valor Lookout", ValorLookoutRoom::new);
    }

    @Override
    public String getName() {
        return "Valor Lookout";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
