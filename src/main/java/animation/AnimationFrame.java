package main.java.animation;

public class AnimationFrame {
    private final int width;
    private final int height;
    private final AnimationChange[][] changes;

    private AnimationFrame(int width, int height, AnimationChange[][] changes) {
        this.width = width;
        this.height = height;
        this.changes = changes;
    }

    public static class Builder {
        private final int width;
        private final int height;
        private final AnimationChange[][] changes;

        public Builder(int width, int height) {
            this.width = width;
            this.height = height;
            changes = new AnimationChange[height][width];
        }

        public Builder set(int x, int y, AnimationChange change) {
            changes[y][x] = change;
            return this;
        }

        public AnimationFrame build() {
            return new AnimationFrame(width, height, changes);
        }
    }
}
