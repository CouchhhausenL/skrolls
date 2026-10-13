package main.java.scene;

import main.java.DifficultyLevel;

import java.util.List;

public abstract class Scene {
    protected final SceneExits exits = new SceneExits();
    protected final DifficultyLevel difficulty;

    protected  Scene(DifficultyLevel difficulty){
        this.difficulty = difficulty;
        setupExits();
    }

    protected abstract void setupExits();
    public abstract String getName();
    public SceneExits getExits(){return exits;}
}
