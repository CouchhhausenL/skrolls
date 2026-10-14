package main.java.scene;

import main.java.GameContext;

public interface SceneResolver {
    Scene resolve(GameContext context);
}
