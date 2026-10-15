package main.java.scene;
import main.java.GameContext;
import main.java.entity.monster.MonsterType;



public abstract class PathScene extends Scene{

    protected PathScene(GameContext gameContext){
        super(gameContext);
    }

    abstract public MonsterType spawnMonster();
}
