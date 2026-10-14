package main.java.scene;

import main.java.DifficultyLevel;
import main.java.GameContext;
import main.java.character.player.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class SceneExits {
    private final Map<Direction, Function<GameContext, Scene>> exits = new EnumMap<>(Direction.class);

    public void addExit(Direction direction, Function<GameContext, Scene> nextSceneResolver){
        exits.put(direction, nextSceneResolver);
    }
}
