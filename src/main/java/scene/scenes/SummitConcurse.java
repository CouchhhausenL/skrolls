package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class SummitConcurse extends PathScene {
    protected SummitConcurse(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_EAST, "Digeterel Summit Base",DigeterelSummitBase::new);
        exits.addExit(Direction.SOUTH_WEST, "Summit-Gilded Junction", SummitGildedJunction::new);
    }

    @Override
    public String getName(){
        return "Summit Concurse";
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
