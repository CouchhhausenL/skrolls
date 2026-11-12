package main.java.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GameStateManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final String DIR_NAME = "skrolls-savestates";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static final Path PRIMARY_DIR = Path.of(DIR_NAME);
    private static final Path FALLBACK_DIR = Path.of(System.getProperty("user.home"), DIR_NAME);

    /**
     * Saves the game with a timestamped filename.
     * Attempts to save into the local 'skrolls-savestates/' directory first;
     * falls back to '~/skrolls-savestates/' in the user's home directory if writing fails.
     */
    public static Path save(Game game) throws IOException {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String fileName = "skrolls_save_" + timestamp + ".json";
        String json = GSON.toJson(new GameState(game));

        Path targetPath;
        try {
            targetPath = writeSaveFile(PRIMARY_DIR, fileName, json);
        } catch (Exception e) {
            System.err.println("Failed to save to primary directory " + PRIMARY_DIR + ", attempting fallback: " + e.getMessage());
            targetPath = writeSaveFile(FALLBACK_DIR, fileName, json);
        }

        game.addMessage("Game saved to " + targetPath);
        return targetPath;
    }

    private static Path writeSaveFile(Path dir, String fileName, String json) throws IOException {
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        Path file = dir.resolve(fileName);
        Files.writeString(file, json);
        return file;
    }

    public static void save(Game game, String filePath) throws IOException {
        GameState state = new GameState(game);
        Path targetPath = Path.of(filePath);
        if (targetPath.getParent() != null && !Files.exists(targetPath.getParent())) {
            Files.createDirectories(targetPath.getParent());
        }
        Files.writeString(targetPath, GSON.toJson(state));
        game.addMessage("Game saved to " + filePath);
    }

    /**
     * Automatically loads the most recent save file from either the primary or fallback directory.
     */
    public static void loadInto(Game game) throws IOException {
        Optional<Path> latestSave = findLatestSaveFile();
        if (latestSave.isEmpty()) {
            game.addMessage("No savegame found in " + PRIMARY_DIR + " or " + FALLBACK_DIR);
            throw new FileNotFoundException("No save file found in " + PRIMARY_DIR + " or " + FALLBACK_DIR);
        }
        loadInto(game, latestSave.get().toString());
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
        return findLatestSaveFile().isPresent();
    }

    public static boolean saveExists(String filePath) {
        return Files.exists(Path.of(filePath));
    }

    /**
     * Finds the most recent save file across both primary and fallback directories.
     */
    public static Optional<Path> findLatestSaveFile() {
        List<Path> all = getAllSaveFiles();
        return all.isEmpty() ? Optional.empty() : Optional.of(all.getFirst());
    }

    /**
     * Retrieves all available save files from both primary and fallback directories,
     * sorted descending by modification time (most recent first).
     */
    public static List<Path> getAllSaveFiles() {
        return Stream.of(PRIMARY_DIR, FALLBACK_DIR)
                .filter(Files::isDirectory)
                .flatMap(dir -> {
                    try {
                        return Files.list(dir);
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                })
                .filter(p -> p.getFileName().toString().endsWith(".json"))
                .distinct()
                .sorted(Comparator.comparingLong((Path p) -> {
                    try {
                        return Files.getLastModifiedTime(p).toMillis();
                    } catch (IOException e) {
                        return 0L;
                    }
                }).reversed())
                .toList();
    }

    /**
     * Reads and parses a GameState from disk without applying it to a running game.
     * Useful for save file inspection and preview screens.
     */
    public static GameState peek(Path path) {
        try {
            String json = Files.readString(path);
            return GSON.fromJson(json, GameState.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formats playtime in milliseconds into a readable representation, e.g. "1 hour 23 minutes 45 seconds".
     */
    public static String formatPlaytime(long millis) {
        long seconds = Math.max(0L, millis / 1000);
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append(hours == 1 ? " hour" : " hours");
        }
        if (minutes > 0) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(minutes).append(minutes == 1 ? " minute" : " minutes");
        }
        if (secs > 0 || sb.isEmpty()) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(secs).append(secs == 1 ? " second" : " seconds");
        }
        return sb.toString();
    }
}
