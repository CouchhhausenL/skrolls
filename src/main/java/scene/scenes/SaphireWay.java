package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class SaphireWay extends PathScene {
    protected SaphireWay(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_EAST, "Clioppexar",Clioppexar::new);
        exits.addExit(Direction.SOUTH_WEST, "Rough Ridge Spire", RoughRidgeSpire::new);
    }

    @Override
    public String getName(){
        return "Saphire Way";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

}
