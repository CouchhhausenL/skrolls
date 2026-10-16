package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Direction;
import main.java.scene.PathScene;

public class MudRoute extends PathScene {
    protected MudRoute(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Juniper Vale", JuniperVale::new);
        exits.addExit(Direction.SOUTH_EAST, "Muddy-Redwood-Archer Crossway", MudRedwoodArcherCrossway::new);
    }

    @Override
    public String getName() {
        return "Mud Route";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

}
