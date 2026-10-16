package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class BrassMapleWood extends LocationScene {
    protected BrassMapleWood(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Lumberway", Lumberway::new);
        exits.addExit(Direction.EAST, "Lumberhighs Approach", LumberhighsApproach::new);
    }

    @Override
    public String getName() {
        return "Brass Maple Wood";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
