package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.CrossroadScene;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class GopherSummitRowanCross extends CrossroadScene {

    protected GopherSummitRowanCross(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.SOUTH_WEST, "Back to Winterfort Castle",Winterfort::new);
        exits.addExit(Direction.EAST, "Rowan Avenue", RowanAvenue::new);
        exits.addExit(Direction.NORTH, "Gopher Connector", GopherConnector::new);
        exits.addExit(Direction.WEST, "Summit Ribbon", SummitRibbon::new);

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

    @Override
    public String getName() {
        return "Gopher-Summit-Rowan Cross";
    }
}
