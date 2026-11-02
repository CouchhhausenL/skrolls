package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.PathScene;

public class GlimountHighlands extends PathScene {

    public GlimountHighlands(GameContext gameContext) {
        super(gameContext);

    }


    @Override
    protected void setupExits() {
        exits.addExit("Ivory Pass", "Leave Highlands back to  Ivory Pass", IvoryPass::new);
    }

    @Override
    public String getName() {
        return "Glimount Highlands";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.EXTREME;
    }
}
