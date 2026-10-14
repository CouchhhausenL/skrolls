package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.CrossroadScene;
import main.java.scene.Direction;

public class MudRedwoodArcherCrossway extends CrossroadScene {

    protected MudRedwoodArcherCrossway(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_WEST, "Mud Route",MudRoute::new);
        exits.addExit(Direction.SOUTH_WEST, "Archer Passage", ArcherPassage::new);
        exits.addExit(Direction.SOUTH_EAST, "Redwood Connector", RedwoodConnector::new);
        exits.addExit(Direction.NORTH_EAST, "Acorn Lane", AcornLane::new);

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Gopher-Summit-Rowan Cross";
    }
}
