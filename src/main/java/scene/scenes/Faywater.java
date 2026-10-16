package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Faywater extends SettlementScene {

    protected Faywater(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.WEST, "Seaside Route", SeasideRoute::new);
        exits.addExit(Direction.NORTH, "Artic Pass", ArcticPass::new);
        exits.addExit("TheAbandinedIsle", "Ship to The Abandined Isle", TheAbandinedIsle::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Faywater";
    }
}
