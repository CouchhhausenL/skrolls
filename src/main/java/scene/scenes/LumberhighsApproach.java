package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class LumberhighsApproach extends PathScene {
    protected LumberhighsApproach(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Brass Maple Wood", BrassMapleWood::new);
        exits.addExit(Direction.EAST, "Lumberhighs-Deepforge Crossing", LumberhighsDeepforgeCrossing::new);
    }

    @Override
    public String getName() {
        return "Lumberhighs Approach";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
