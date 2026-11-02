package main.java.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameStateManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final String DEFAULT_SAVE_FILE = "savegame.json";

    public static void save(Game game) throws IOException {
        save(game, DEFAULT_SAVE_FILE);
    }

    public static void save(Game game, String filePath) throws IOException {
        GameState state = new GameState(game);
        Files.writeString(Path.of(filePath), GSON.toJson(state));
        game.addMessage("Game saved to " + filePath);
    }

    public static void loadInto(Game game) throws IOException {
        loadInto(game, DEFAULT_SAVE_FILE);
    }

    public static void loadInto(Game game, String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            game.addMessage("No savegame found at " + filePath);
            throw new FileNotFoundException("Save file not found: " + filePath);
        }
        String json = Files.readString(path);
        GameState state = GSON.fromJson(json, GameState.class);
        state.applyTo(game);
        game.addMessage("Game loaded from " + filePath);
    }

    public static boolean saveExists() {
        return saveExists(DEFAULT_SAVE_FILE);
    }

    public static boolean saveExists(String filePath) {
        return Files.exists(Path.of(filePath));
    }
}
