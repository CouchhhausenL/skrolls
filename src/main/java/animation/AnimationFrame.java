package main.java.animation;

import main.java.console.CellBuffer;

public class AnimationFrame {
    private final int width;
    private final int height;
    private final AnimationChange[][] changes;

    private AnimationFrame(int width, int height, AnimationChange[][] changes) {
        this.width = width;
        this.height = height;
        this.changes = changes;
    }

    private AnimationChange get(int x, int y) {
        return changes[y][x];
    }

    public void overlayOntoBuffer(CellBuffer buffer) {
        if (buffer.getWidth() != width || buffer.getHeight() != height)
            throw new IllegalArgumentException("Buffer dimensions do not match animation dimensions");

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int fx = x;
                final int fy = y;

                AnimationChange change = get(x, y);
                if (change == null) continue;
                change.ifCharacter(c -> buffer.setCharacter(fx, fy, c));
                change.ifForeground(f -> buffer.setForeground(fx, fy, f));
                change.ifBackground(b -> buffer.setBackground(fx, fy, b));
            }
        }

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
