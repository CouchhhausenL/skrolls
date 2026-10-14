package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.Scene;

public class Winterfort extends Scene {

    protected Winterfort(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH, Winterfort::new);

        /*
        exits.addExit(Direction.NW, gameContext -> new GopherByway());
        exits.addExit(Direction.E, gameContext -> new Lumberway());
        exits.addExit(Direction.N, gameContext -> new WinterfortCastle());
        exits.addExit(Direction.NE, gameContext -> new GuildedApproach());
        exits.addExit(Direction.SW, gameContext -> new MerchantAvenue());
        exits.addExit(Direction.SE, gameContext -> new WinterLane());
        */

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.NONE;
    }

    @Override
    public String getName() {
        return "Winterfort";
    }
}
