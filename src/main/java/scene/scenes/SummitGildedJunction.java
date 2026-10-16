package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.CrossroadScene;
import main.java.scene.Direction;

public class SummitGildedJunction extends CrossroadScene {

    protected SummitGildedJunction(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_WEST, "Gilded Route", GildedRoute::new);
        exits.addExit(Direction.NORTH_EAST, "Summit Concurse", SummitConcurse::new);
        exits.addExit(Direction.SOUTH_WEST, "Gilded Nexus", GildedNexus::new);

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

    @Override
    public String getName() {
        return "Summit-Gilded Junction";
    }
}
