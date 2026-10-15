package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class MeadowTrace extends PathScene {
    protected MeadowTrace(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_WEST, "Highland-Meadow Fork",HighlandMeadowFork::new);
        exits.addExit(Direction.NORTH_EAST, "Shaucroft Hollows", ShaucroftHollows::new);
    }

    @Override
    public String getName(){
        return "Meadow Trace";
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
