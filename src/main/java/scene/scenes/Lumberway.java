package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class Lumberway extends PathScene {
    protected Lumberway(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Winterfort", Winterfort::new);
        exits.addExit(Direction.EAST, "Brass Maple Wood", BrassMapleWood::new);
    }

    @Override
    public String getName() {
        return "Lumberway";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

}
