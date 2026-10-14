package main.java.scene.exit;

import main.java.scene.Direction;
import main.java.scene.SceneResolver;

public class DirectionalSceneExit extends AbstractSceneExit {
    private final Direction direction;

    public DirectionalSceneExit(Direction direction, String displayName, SceneResolver resolver) {
        super(resolver, displayName);
        this.direction = direction;
    }

    @Override
    public String getId() {
        return direction.getId();
    }
}
