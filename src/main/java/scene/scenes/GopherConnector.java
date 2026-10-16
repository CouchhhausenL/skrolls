package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class GopherConnector extends PathScene {
    protected GopherConnector(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_EAST, "The Score Gopher",TheScoreGopher::new);
        exits.addExit(Direction.NORTH_WEST,"Highland-Meadow Fork", HighlandMeadowFork::new);
    }

    @Override
    public String getName(){
        return "Gopher Connector";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

}
