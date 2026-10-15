package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.LocationScene;

public class RedwoodWoodlands extends LocationScene {
    protected RedwoodWoodlands(GameContext gameContext) {
        super(gameContext);
    }
    @Override
    protected void setupExits(){
        exits.addExit("Exowerin", "Leave Forest, back to Exowerin",Exowerin::new);
    }

    @Override
    public String getName(){
        return "Redwood Woodlands";
    }

    @Override
    public DifficultyLevel getDifficulty(){
        return DifficultyLevel.MEDIUM;
    }

}
