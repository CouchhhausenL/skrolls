package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class GildedRoute extends PathScene {
    protected GildedRoute(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Ashwick", Ashwick::new);
        exits.addExit(Direction.SOUTH_EAST, "Summit-Gilded Junction", SummitGildedJunction::new);
    }

    @Override
    public String getName() {
        return "Gilded Route";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
