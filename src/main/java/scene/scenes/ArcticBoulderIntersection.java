package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class ArcticBoulderIntersection extends PathScene {
    protected ArcticBoulderIntersection(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.EAST, "Little Passage",LittlePassage::new);
        exits.addExit(Direction.NORTH,"Boulder Route", BoulderRoute::new);
        exits.addExit(Direction.SOUTH,"Arctic Pass", ArcticPass::new);
    }

    @Override
    public String getName(){
        return "Arctic-Boulder Intersection";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public MonsterType spawnMonster() {
        return MonsterType.AXE_ROGUE;
    }
}
