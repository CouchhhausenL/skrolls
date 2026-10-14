package main.java.scene.exit;

import main.java.scene.SceneResolver;

public class SceneExit extends AbstractSceneExit {
    private final String id;

    public SceneExit(String id, SceneResolver resolver) {
        super(resolver);
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
