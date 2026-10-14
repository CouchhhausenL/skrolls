package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class GildedNexus extends PathScene {
    protected GildedNexus(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_WEST, "Summit-Gilded Junction",SummitGildedJunction::new);
        exits.addExit(Direction.SOUTH_EAST, "The Happy Fly Pub", TheHappyFlyPub::new);
    }

    @Override
    public String getName(){
        return "Gilded Nexus";
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
