package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class RowanConnector extends PathScene {
    protected RowanConnector(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.WEST, "Musterfield",Musterfield::new);
        exits.addExit(Direction.EAST, "Rough Ridge Spire", RoughRidgeSpire::new);
    }

    @Override
    public String getName(){
        return "RowanConnector";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.LOW;
    }

}
