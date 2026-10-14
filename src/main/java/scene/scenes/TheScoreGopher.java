package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.monster.MonsterType;
import main.java.scene.CrossroadScene;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class TheScoreGopher extends CrossroadScene {
    protected TheScoreGopher(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit(Direction.SOUTH_WEST, "Gopher Byway",GopherByway::new);
        exits.addExit(Direction.WEST, "Summit Ribbon", SummitRibbon::new);
        exits.addExit(Direction.NORTH_WEST, "Gopher Connector", GopherConnector::new);
    }

    @Override
    public String getName(){
        return "The Score Gopher";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.LOW;
    }


}
