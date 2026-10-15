package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class ArcherPassage extends PathScene {
    protected ArcherPassage(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_WEST, "Exowerin",Exowerin::new);
        exits.addExit(Direction.NORTH_EAST, "Muddy-Redwood-Archer Crossway", MudRedwoodArcherCrossway::new);
    }

    @Override
    public String getName(){
        return "Archer Passage";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.HIGH;
    }

    @Override
    public MonsterType spawnMonster() {
        return MonsterType.ORK;
    }
}
