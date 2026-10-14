package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.scene.CrossroadScene;
import main.java.scene.Direction;

public class DigeterelSummitBase extends CrossroadScene {

    protected DigeterelSummitBase(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {

        exits.addExit("DigeterelSummit", "Climb Digeterel Summit",DigeterelSummit::new);
        exits.addExit(Direction.EAST, "Summit Scarp", SummitScarp::new);
        exits.addExit(Direction.SOUTH_WEST, "Summit Concurse", SummitConcurse::new);

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

    @Override
    public String getName() {
        return "Digeterel Summit Base";
    }
}
