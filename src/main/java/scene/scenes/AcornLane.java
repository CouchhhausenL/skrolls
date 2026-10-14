package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class AcornLane extends PathScene {
    protected AcornLane(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_WEST, "Muddy-Redwood-Archer Crossway",MudRedwoodArcherCrossway::new);
        exits.addExit(Direction.NORTH_EAST,"Sapling Ford", SaplingFord::new);
    }

    @Override
    public String getName(){
        return "Acorn Lane";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public MonsterType spawnMonster() {
        return MonsterType.ORK;
    }
}
