package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class GranitRoute extends PathScene {
    protected GranitRoute(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_WEST, "Rough Ridge Spire Crossing",RoughRidgeSpire::new);
        exits.addExit(Direction.SOUTH_EAST, "Silverhollow", Silverhollow::new);
    }

    @Override
    public String getName(){
        return "GranitRoute";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

}
