package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class CathedralLane extends PathScene {
    protected CathedralLane(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.WEST, "Valor Lookout",ValorLookout::new);
        exits.addExit(Direction.EAST,"Kronomore", Kronomore::new);
        exits.addExit("Thessahampton Hills", "Explore Thessahampton Hills", ThessahamptonHills::new);
    }

    @Override
    public String getName(){
        return "Cathedral Lane";
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
