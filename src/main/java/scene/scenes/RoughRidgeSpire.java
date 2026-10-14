package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class RoughRidgeSpire extends LocationScene {
    protected RoughRidgeSpire(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_EAST, "Granit Route",GranitRoute::new);
        exits.addExit(Direction.WEST, "Rowan Connector",RowanConnector::new);
        exits.addExit(Direction.NORTH_EAST, "Saphire Way",SaphireWay::new);
    }

    @Override
    public String getName(){
        return "Rough Ridge Spire";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

}
