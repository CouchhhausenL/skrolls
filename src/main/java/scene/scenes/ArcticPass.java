package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class ArcticPass extends PathScene {
    protected ArcticPass(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH, "Faywater", Faywater::new);
        exits.addExit(Direction.NORTH, "Arctic-Boulder Intersection", ArcticBoulderIntersection::new);
    }

    @Override
    public String getName() {
        return "Arctic Pass";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
