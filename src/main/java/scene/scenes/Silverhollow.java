package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Silverhollow extends SettlementScene {

    protected Silverhollow(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_WEST, "Granit Route", GranitRoute::new);
        exits.addExit(Direction.SOUTH, "Ivory Pass", IvoryPass::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Silverhollow";
    }
}
