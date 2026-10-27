package main.java.animation;

import main.java.console.CellBuffer;

import java.util.ArrayList;
import java.util.List;

// Immutable animation loaded from a .ska file: shared canvas size,
// background color, playback speed and per-frame pixel changes.
public class Animation {
    private final int width;
    private final int height;
    private final int background;
    private final int fps;
    private final AnimationFrame[] frames;

    private Animation(int width, int height, int background, int fps, AnimationFrame[] frames) {
        this.width = width;
        this.height = height;
        this.background = background;
        this.fps = fps;
        this.frames = frames;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFrameCount() {
        return frames.length;
    }

    public int getBackground() {
        return background;
    }

    public int getFps() {
        return fps;
    }

    public AnimationFrame getFrame(int frame) {
        return frames[frame];
    }

    public void overlayFrameOntoBuffer(CellBuffer buffer, int frame) {
        getFrame(frame).overlayOntoBuffer(buffer);
    }

    // Resets the playback buffer to fully transparent so that pixels not
    // explicitly set by any frame are invisible (terminal background shows through).
    public void initBuffer(CellBuffer buffer) {
        buffer.fill(CellBuffer.EMPTY_CELL, 0xFFFFFF, CellBuffer.TRANSPARENT_BACKGROUND);
    }

    // Mutable builder used by SkaParser while frames are read
    public static class Builder {
        private final int width;
        private final int height;
        private final int background;
        private final int fps;
        private final List<AnimationFrame> frames;

        public Builder(int width, int height, int background, int fps) {
            this.width = width;
            this.height = height;
            this.background = background;
            this.fps = fps;
            this.frames = new ArrayList<>();
        }

        public Builder frame(AnimationFrame frame) {
            frames.add(frame);
            return this;
        }

        public Animation build() {
            return new Animation(width, height, background, fps, frames.toArray(AnimationFrame[]::new));
        }
    }
}
