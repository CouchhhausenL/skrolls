package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.CrossroadScene;
import main.java.scene.Direction;

public class LumberhighsDeepforgeCrossing extends CrossroadScene {

    protected LumberhighsDeepforgeCrossing(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.SOUTH, "Boulder Route", BoulderRoute::new);
        exits.addExit(Direction.EAST, "Deepforge", Deepforge::new);
        exits.addExit(Direction.WEST, "Lumberhighs Approach", LumberhighsApproach::new);
        exits.addExit(Direction.NORTH, "Ivory Pass", IvoryPass::new);

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Lumberhighs-Deepforge Crossing";
    }
}
