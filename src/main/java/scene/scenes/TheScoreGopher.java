package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.LocationScene;

public class TheScoreGopher extends LocationScene {
    protected TheScoreGopher(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH_WEST, "Gopher Byway", GopherByway::new);
        exits.addExit(Direction.WEST, "Summit Ribbon", SummitRibbon::new);
        exits.addExit(Direction.NORTH_WEST, "Gopher Connector", GopherConnector::new);
        exits.addExit(Direction.EAST, "Rowan Avenue", RowanAvenue::new);
        exits.addExit("Enter The Score Gopher", "Enter The Score Gopher", TheScoreGopherRoom::new);
    }

    @Override
    public String getName() {
        return "The Score Gopher";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.NONE;
    }


}
