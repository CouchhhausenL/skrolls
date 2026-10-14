package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class MemorialShorePillar extends LocationScene {
    protected MemorialShorePillar(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.EAST, "Little Passage",LittlePassage::new);
    }

    @Override
    public String getName(){
        return "Little Passage";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.HIGH;
    }

}
