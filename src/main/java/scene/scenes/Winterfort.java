package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Winterfort extends SettlementScene {

    protected Winterfort(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_EAST, "Gopher Byway", GopherByway::new);
        exits.addExit(Direction.EAST, "Lumberway", Lumberway::new);
        exits.addExit(Direction.SOUTH_EAST, "Winter Lane", WinterLane::new);
        exits.addExit(Direction.SOUTH_WEST, "Merchant Avenue", MerchantAvenue::new);
        exits.addExit(Direction.NORTH_WEST, "Gilded Approach", GildedApproach::new);
        exits.addExit("WinterfortCastle", "Enter Winterfort Castle", WinterfortCastle::new);


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
