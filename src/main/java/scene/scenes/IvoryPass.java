package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class IvoryPass extends PathScene {
    protected IvoryPass(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH, "Silverhollow",Silverhollow::new);
        exits.addExit(Direction.SOUTH, "Lumberhighs-Deepforge Crossing", LumberhighsDeepforgeCrossing::new);
    }

    @Override
    public String getName(){
        return "Ivory Pass";
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
