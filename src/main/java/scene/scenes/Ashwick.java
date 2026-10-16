package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Ashwick extends SettlementScene {

    protected Ashwick(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_EAST, "Highland Boulevard", HighlandBoulevard::new);
        exits.addExit(Direction.SOUTH_WEST, "Silt Passage", SiltPassage::new);
        exits.addExit(Direction.SOUTH_EAST, "Gilded Route", GildedRoute::new);
        exits.addExit("SoulObelisk", "Enter Soul Obelisk", SoulObelisk::new);
        exits.addExit("Treacherous Forest", "Go to surrounding Treacherous Forest", TreacherousForest::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Ashwick";
    }
}
