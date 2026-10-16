package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class LittlePassage extends PathScene {
    protected LittlePassage(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.EAST, "Memorial Shore Pillar",MemorialShorePillar::new);
        exits.addExit(Direction.WEST, "Arctic-Boulder Intersection", ArcticBoulderIntersection::new);
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
