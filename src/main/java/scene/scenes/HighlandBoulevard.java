package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class HighlandBoulevard extends PathScene {
    protected HighlandBoulevard(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_EAST, "Falconedge",Falconedge::new);
        exits.addExit(Direction.SOUTH_WEST, "Ashwick", Ashwick::new);
    }

    @Override
    public String getName(){
        return "Highland Boulevard";
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
