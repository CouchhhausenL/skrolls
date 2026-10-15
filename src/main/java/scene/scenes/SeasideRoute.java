package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class SeasideRoute extends PathScene {
    protected SeasideRoute(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.WEST, "Winterfort",Winterfort::new);
        exits.addExit(Direction.EAST,"Faywater", Faywater::new);
    }

    @Override
    public String getName(){
        return "Seaside Route";
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
