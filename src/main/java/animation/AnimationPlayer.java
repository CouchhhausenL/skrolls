package main.java.animation;

import main.java.console.CellBuffer;

// Advances an Animation over time and composites each frame into its own buffer.
// Timing is based on System.nanoTime() and does not accumulate drift.
public class AnimationPlayer {
    private static final long ONE_SECOND_IN_NANO = 1_000_000_000L;

    private final Animation animation;
    private final CellBuffer buffer;
    private final int frameCount;
    private final long frameDuration;
    private int frame = 0;
    private boolean looping = false;
    private boolean paused = false;

    long lastFrameTime = System.nanoTime();

    public AnimationPlayer(Animation animation) {
        this.animation = animation;
        this.buffer = new CellBuffer(animation.getWidth(), animation.getHeight());
        frameCount = animation.getFrameCount();
        frameDuration = ONE_SECOND_IN_NANO / animation.getFps();
        resetFrame();
    }

    public void resetFrame() {
        frame = 0;
        animation.initBuffer(buffer);
        animation.overlayFrameOntoBuffer(buffer, frame);
    }

    private void nextFrame() {
        frame++;
        animation.overlayFrameOntoBuffer(buffer, frame);
    }

    private boolean isLastFrame() {
        return frameCount - 1 == frame;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void pause() {
        setPaused(true);
    }

    public void resume() {
        setPaused(false);
    }

    public boolean isDone() {
        return frame == frameCount - 1 && !looping;
    }

    public void update() {
        if (paused) return;
        long now = System.nanoTime();

        // Advance at most one frame per update; the next update catches up
        if (now - lastFrameTime > frameDuration) {
            if (isLastFrame()) {
                if (!looping) return;
                resetFrame();
            } else {
                nextFrame();
            }
            lastFrameTime = now;
        }
    }

    public CellBuffer getCurrentBuffer() {
        return buffer;
    }
}
