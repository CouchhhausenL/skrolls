package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class RowanAvenue extends PathScene {
    protected RowanAvenue(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_WEST, "The Score Gopher",TheScoreGopher::new);
        exits.addExit(Direction.NORTH_EAST, "Musterfield", Musterfield::new);
    }

    @Override
    public String getName(){
        return "Rowan Avenue";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.LOW;
    }

    @Override
    public MonsterType spawnMonster() {
        return MonsterType.ORK;
    }
}
