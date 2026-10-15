package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class CelestialRill extends PathScene {
    protected CelestialRill(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.WEST, "Kronomore",Kronomore::new);
        exits.addExit(Direction.EAST, "The Chilly Cliff Pub", TheChillyCliffPub::new);
    }

    @Override
    public String getName(){
        return "Celestial Rill";
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
