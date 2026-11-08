package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class SearnistShores extends PathScene {
    protected SearnistShores(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Faywater", Faywater::new);
        exits.addExit(Direction.SOUTH_EAST, "The Abandined Isle", TheAbandinedIsle::new);
    }

    @Override
    public String getName() {
        return "Searnist Shores";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
