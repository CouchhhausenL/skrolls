package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Deepforge extends SettlementScene {

    protected Deepforge(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.WEST, "Lumberhighs-Deepforge Crossing", LumberhighsDeepforgeCrossing::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }

    @Override
    public String getName() {
        return "Deepforge";
    }
}
