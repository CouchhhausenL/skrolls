package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class WinterfortCastle extends LocationScene {
    protected WinterfortCastle(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit("Winterfort", "Leave Winterfort Castle",Winterfort::new);
    }

    @Override
    public String getName(){
        return "Winterforst Castle";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.LOW;
    }

}
