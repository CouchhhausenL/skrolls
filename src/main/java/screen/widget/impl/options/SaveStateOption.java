package main.java.screen.widget.impl.options;

import java.nio.file.Path;

/**
 * An option representing a save state file on disk in an OptionsWidget.
 */
public class SaveStateOption extends Option {
    private final Path savePath;

    public SaveStateOption(String name, int color, Path savePath, Runnable runnable) {
        super(name, color, runnable);
        this.savePath = savePath;
    }

    public Path getSavePath() {
        return savePath;
    }
}
