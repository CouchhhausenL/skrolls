package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class RedwoodConnector extends PathScene {
    protected RedwoodConnector(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.NORTH_WEST, "Muddy-Redwood-Archer Crossway",JuniperVale::new);
        exits.addExit(Direction.SOUTH_EAST, "Valor Lookout", ValorLookout::new);
    }

    @Override
    public String getName(){
        return "Redwood Connector";
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
