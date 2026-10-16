package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;

public class Exowerin extends SettlementScene {

    protected Exowerin(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_EAST, "Archer Passage", ArcherPassage::new);
        exits.addExit("TheYggdrasoul", "Visit The Yggdrasoul Elder Tree", TheYggdrasoul::new);
        exits.addExit("Redwood Woodlands", "Go to surrounding Redwood Woodlands", RedwoodWoodlands::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }

    @Override
    public String getName() {
        return "Exowerin";
    }
}
