package main.java.scene.exit;

import main.java.scene.Direction;
import main.java.scene.SceneResolver;

import java.util.HashSet;
import java.util.Set;

public class SceneExits {
    private final Set<AbstractSceneExit> exits = new HashSet<>();

    public void addExit(AbstractSceneExit exit) {
        exits.add(exit);
    }
    public void addExit(String id, SceneResolver resolver) {
        addExit(new SceneExit(id, resolver));
    }
    public void addExit(Direction direction, SceneResolver resolver) {
        addExit(new DirectionalSceneExit(direction, resolver));
    }
}
