package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class MerchantAvenue extends PathScene {
    protected MerchantAvenue(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_EAST, "Winterfort",Winterfort::new);
        exits.addExit(Direction.SOUTH_WEST, "Sapling Ford", SaplingFord::new);
    }

    @Override
    public String getName(){
        return "Merchant Avenue";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.LOW;
    }

    @Override
    public MonsterType spawnMonster() {
        return MonsterType.AXE_ROGUE;
    }
}
