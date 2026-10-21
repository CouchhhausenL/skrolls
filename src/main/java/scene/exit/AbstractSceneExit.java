package main.java.scene.exit;

import main.java.DifficultyLevel;
import main.java.game.GameContext;
import main.java.scene.Scene;
import main.java.scene.SceneResolver;

public abstract class AbstractSceneExit {
    private final SceneResolver resolver;
    private final String displayName;

    public AbstractSceneExit(SceneResolver resolver, String displayName) {
        this.resolver = resolver;
        this.displayName = displayName;
    }

    public Scene resolve(GameContext context) {
        return resolver.resolve(context);
    }


    public String getDisplayName() {
        return displayName;
    }


    public abstract String getId();
}
