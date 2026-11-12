package main.java.screen.impl;

import main.java.console.input.KeyInputEvent;
import main.java.game.Game;
import main.java.game.GameContext;
import main.java.game.GameState;
import main.java.game.GameStateManager;
import main.java.screen.Screen;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.screen.widget.impl.options.Option;
import main.java.screen.widget.impl.options.SaveStateOption;
import main.java.util.Color;
import main.java.util.SFXPlayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Screen displaying all available save states.
 * Allows the user to browse save states with a live detail preview
 * and load a chosen state. The most recent save is selected by default.
 */
public class LoadGameScreen extends Screen {

    private static final Pattern SAVE_NAME_PATTERN = Pattern.compile("skrolls_save_(\\d{4}-\\d{2}-\\d{2})_(\\d{2})-(\\d{2})-(\\d{2})\\.json");

    private Game game;
    private GameContext context;
    private OptionsWidget optionsWidget;
    private ScrollableContainerWidget details;
    private LabelWidget headerWidget;
    private LabelWidget hintWidget;

    public LoadGameScreen() {
    }

    public LoadGameScreen(Game game) {
        this.game = game;
    }

    @Override
    protected void init(GameContext context) {
        this.context = context;
        if (this.game == null && context instanceof Game g) {
            this.game = g;
        }

        buildUI();
    }

    private void buildUI() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        headerWidget = new LabelWidget(0, 0, "=== Load Game - Select a Save State ===", Color.YELLOW);
        addChild(headerWidget);

        int leftWidth = Math.min(48, Math.max(36, screenWidth / 2 - 2));
        int contentHeight = Math.max(5, screenHeight - 4);

        details = new ScrollableContainerWidget(leftWidth + 2, 2, Math.max(30, screenWidth - (leftWidth + 3)), contentHeight, 0);
        details.setKeyboardInput(false);
        details.setOnlyScrollOnOverflow(true);
        addChild(details);

        hintWidget = new LabelWidget(0, screenHeight - 1, "Press [Space] / [Enter] to Load  |  [Escape] to Cancel", Color.BG5);
        addChild(hintWidget);

        drawSaveOptions(leftWidth, contentHeight);
    }

    private void drawSaveOptions(int leftWidth, int contentHeight) {
        if (optionsWidget != null) {
            removeChild(optionsWidget);
        }

        optionsWidget = new OptionsWidget(0, 2, leftWidth, contentHeight);
        optionsWidget.setOnSelectionChanged(option -> {
            if (option instanceof SaveStateOption saveOption) {
                showDetails(saveOption.getSavePath());
            }
        });

        List<Path> saveFiles = GameStateManager.getAllSaveFiles();

        if (saveFiles.isEmpty()) {
            optionsWidget.addOption(new Option("----- No Save States Found -----", Color.BG5, null));
            details.clearChildren();
            details.addChild(new LabelWidget("No save states found.", Color.BG5));
            details.addChild(new LabelWidget("Save files are searched in:", Color.BG4));
            details.addChild(new LabelWidget("- skrolls-savestates/"));
            details.addChild(new LabelWidget("- " + System.getProperty("user.home") + "/skrolls-savestates/"));
        } else {
            for (int i = 0; i < saveFiles.size(); i++) {
                Path savePath = saveFiles.get(i);
                boolean isLatest = (i == 0);
                String displayName = formatSaveName(savePath.getFileName().toString(), isLatest);
                int color = isLatest ? Color.GREEN : Color.BEIGE;

                optionsWidget.addOption(new SaveStateOption(
                        displayName,
                        color,
                        savePath,
                        () -> loadSelected(savePath)
                ));
            }
        }

        addChild(optionsWidget);
    }

    private String formatSaveName(String fileName, boolean isLatest) {
        Matcher matcher = SAVE_NAME_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            String date = matcher.group(1);
            String time = matcher.group(2) + ":" + matcher.group(3) + ":" + matcher.group(4);
            return date + " " + time + (isLatest ? " [Latest]" : "");
        }
        return fileName + (isLatest ? " [Latest]" : "");
    }

    private void showDetails(Path savePath) {
        details.clearChildren();
        if (savePath == null || !Files.exists(savePath)) {
            details.addChild(new LabelWidget("No save state selected.", Color.BG5));
            return;
        }

        GameState state = GameStateManager.peek(savePath);

        details.addChild(new LabelWidget("Save State Details", Color.BEIGE));
        details.addChild(new LabelWidget("----------------------------------------", Color.BG4));
        details.addChild(new LabelWidget("File: " + savePath.getFileName().toString(), Color.WHITE));

        String homeDir = Path.of(System.getProperty("user.home")).toAbsolutePath().toString();
        boolean inHome = savePath.toAbsolutePath().toString().startsWith(homeDir);
        String location = inHome ? "User Home (~/skrolls-savestates)" : "Project Directory (skrolls-savestates)";
        details.addChild(new LabelWidget("Folder: " + location, Color.BG5));

        try {
            String modified = Files.getLastModifiedTime(savePath).toString();
            details.addChild(new LabelWidget("Modified: " + modified.replace("T", " ").replaceAll("\\..*", ""), Color.BG5));
        } catch (IOException ignored) {
        }

        details.addChild(new LabelWidget(""));

        if (state != null) {
            details.addChild(new LabelWidget("--- Character ---", Color.GREEN));
            details.addChild(new LabelWidget("Player: " + (state.playerName != null ? state.playerName : "Unknown"), Color.WHITE));
            details.addChild(new LabelWidget("Level: " + (state.playerLevel != null ? state.playerLevel.name() : "N/A"), Color.YELLOW));
            details.addChild(new LabelWidget("Playtime: " + GameStateManager.formatPlaytime(state.playtimeMillis), Color.LIGHT_BLUE));
            details.addChild(new LabelWidget("Magic Shards: " + state.magicShards, Color.CYAN));

            details.addChild(new LabelWidget(""));
            details.addChild(new LabelWidget("--- Progress ---", Color.GREEN));
            String sceneName = "Unknown";
            if (state.sceneClassName != null) {
                sceneName = state.sceneClassName.substring(state.sceneClassName.lastIndexOf('.') + 1);
            }
            details.addChild(new LabelWidget("Current Scene: " + sceneName, Color.BEIGE));
            details.addChild(new LabelWidget("Active Quests: " + (state.quests != null ? state.quests.size() : 0), Color.WHITE));
            details.addChild(new LabelWidget("Backpack Items: " + (state.backpack != null ? state.backpack.size() : 0), Color.WHITE));
        } else {
            details.addChild(new LabelWidget("Could not parse save file contents.", Color.RED));
        }

        details.addChild(new LabelWidget(""));
        details.addChild(new LabelWidget("-> Press [Space] or [Enter] to load", Color.GREEN));
    }

    private void loadSelected(Path savePath) {
        try {
            if (game != null) {
                GameStateManager.loadInto(game, savePath.toString());
            } else if (context instanceof Game g) {
                GameStateManager.loadInto(g, savePath.toString());
            } else {
                throw new IllegalStateException("Game instance is not available to load save state.");
            }
            SFXPlayer.playMenuSelect();
            context.setScreen(new MainGameScreen());
        } catch (Exception e) {
            SFXPlayer.playNegative();
            context.addMessage("Failed to load savegame: " + e.getMessage());
            context.setScreen(new MainGameScreen());
        }
    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isEscape()) {
            context.setScreen(new MainGameScreen());
        }
    }

    @Override
    public void onResize() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        if (headerWidget != null) {
            headerWidget.setPosition(0, 0);
        }
        if (hintWidget != null) {
            hintWidget.setPosition(0, screenHeight - 1);
        }

        int leftWidth = Math.min(48, Math.max(36, screenWidth / 2 - 2));
        int contentHeight = Math.max(5, screenHeight - 4);

        if (details != null) {
            details.setPosition(leftWidth + 2, 2);
            details.setDimensions(Math.max(30, screenWidth - (leftWidth + 3)), contentHeight);
        }

        drawSaveOptions(leftWidth, contentHeight);
    }
}
