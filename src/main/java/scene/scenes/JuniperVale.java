package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class JuniperVale extends SettlementScene {

    protected JuniperVale(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_EAST, "Silt Passage", SiltPassage::new);
        exits.addExit(Direction.SOUTH_WEST, "Mud Route", MudRoute::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "JuniperVale";
    }
}
