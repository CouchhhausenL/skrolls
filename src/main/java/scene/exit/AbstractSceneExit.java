package main.java.scene.exit;

import main.java.GameContext;
import main.java.scene.Scene;
import main.java.scene.SceneResolver;

public abstract class AbstractSceneExit {
    private final SceneResolver resolver;

    public AbstractSceneExit(SceneResolver resolver) {
        this.resolver = resolver;
    }

    public Scene resolve(GameContext context) {
        return resolver.resolve(context);
    }

    public abstract String getId();
}
