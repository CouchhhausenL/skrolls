package main.java.scene.exit;

import main.java.scene.SceneResolver;

public class SceneExit extends AbstractSceneExit {
    private final String id;

    public SceneExit(String id, String displayName, SceneResolver resolver) {
        super(resolver, displayName);
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
