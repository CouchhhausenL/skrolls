package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.scene.Direction;
import main.java.scene.Scene;

public class Winterfort extends Scene {

    public Winterfort(){
        super(DifficultyLevel.NONE);
    }

    @Override
    protected void setupExits() {
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
    public String getName() {
        return "Winterfort";
    }


}
