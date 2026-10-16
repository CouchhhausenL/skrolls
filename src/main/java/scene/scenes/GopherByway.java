package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class GopherByway extends PathScene {
    protected GopherByway(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_EAST, "Gopher-Summit-Rowan Cross",GopherSummitRowanCross::new);
        exits.addExit(Direction.SOUTH_WEST, "Winterfort", Winterfort::new);
    }

    @Override
    public String getName(){
        return "Gopher Byway";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.LOW;
    }

}
