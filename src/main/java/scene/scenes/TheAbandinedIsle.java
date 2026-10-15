package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.SettlementScene;

public class TheAbandinedIsle extends SettlementScene {

    protected TheAbandinedIsle(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit("Faywater", "Ship back to Faywater", Faywater::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }

    @Override
    public String getName() {
        return "The Abandined Isle";
    }
}
