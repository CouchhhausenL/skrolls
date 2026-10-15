package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class SiltPassage extends PathScene {
    protected SiltPassage(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_EAST, "Ashwick",Ashwick::new);
        exits.addExit(Direction.SOUTH_WEST, "Juniper Vale", JuniperVale::new);
    }

    @Override
    public String getName(){
        return "Silt Passage";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public MonsterType spawnMonster() {
        return MonsterType.SKELETON;
    }
}
