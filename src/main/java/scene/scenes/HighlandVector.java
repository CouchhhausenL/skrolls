package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class HighlandVector extends PathScene {
    protected HighlandVector(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_EAST, "HighlandMeadowFork",HighlandMeadowFork::new);
        exits.addExit(Direction.NORTH_WEST, "Falconedge", Falconedge::new);
    }

    @Override
    public String getName(){
        return "Highland Vector";
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
