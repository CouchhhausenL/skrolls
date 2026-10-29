package main.java.scene;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.game.GameContext;
import main.java.scene.exit.SceneExits;

import java.util.List;

public abstract class Scene {
    protected final SceneExits exits = new SceneExits();
    protected GameContext gameContext;

    protected Scene(GameContext gameContext) {
        setupExits();
        this.gameContext = gameContext;
    }

    protected abstract void setupExits();

    public abstract DifficultyLevel getDifficulty();

    public abstract String getName();

    public SceneExits getExits() {
        return exits;
    }
    public List<Interactable> getInteractables(){
        return List.of();
    }
}
