package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class BoulderRoute extends PathScene {
    protected BoulderRoute(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH, "Arctic-Boulder Intersection",ArcticBoulderIntersection::new);
        exits.addExit(Direction.NORTH, "Lumberhighs-Deepforge Crossing", LumberhighsDeepforgeCrossing::new);
    }

    @Override
    public String getName(){
        return "Boulder Route";
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
