package main.java.scene;

import main.java.game.GameContext;

public interface SceneResolver {
    Scene resolve(GameContext context);
}
