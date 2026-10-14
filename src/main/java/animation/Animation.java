package main.java.animation;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    private final int width;
    private final int height;
    private final AnimationFrame[] frames;

    private Animation(int width, int height, AnimationFrame[] frames) {
        this.width = width;
        this.height = height;
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
    public AnimationFrame getFrame(int frame) {
        return frames[frame];
    }

    public static class Builder {
        private final int width;
        private final int height;
        private final List<AnimationFrame> frames;

        public Builder(int width, int height) {
            this.width = width;
            this.height = height;
            this.frames = new ArrayList<>();
        }

        public Builder frame(AnimationFrame frame) {
            frames.add(frame);
            return this;
        }

        public Animation build() {
            return new Animation(width, height, frames.toArray(AnimationFrame[]::new));
        }
    }
}
