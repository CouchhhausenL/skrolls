package main.java.screen.widget.impl;

import main.java.animation.Animation;
import main.java.animation.AnimationPlayer;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;

// Widget that plays an animation and optionally notifies when it finishes
public class AnimationWidget extends Widget {
    private final AnimationPlayer player;
    private OnDone onDone = null;

    public AnimationWidget(int x, int y, int width, int height, Animation animation) {
        super(x, y, width, height);
        player = new AnimationPlayer(animation);
    }

    public AnimationWidget(int x, int y, Animation animation) {
        this(x, y, animation.getWidth(), animation.getHeight(), animation);
    }

    public AnimationWidget onDone(OnDone onDone) {
        this.onDone = onDone;
        return this;
    }

    @Override
    public void onDraw(DrawContextView context) {
        // Advance playback once per frame, then report completion
        player.update();
        if (player.isDone() && onDone != null) onDone.onDone();
        context.drawBuffer(0, 0, player.getCurrentBuffer());
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {

    }


    @Override
    public void onMouseInput(MouseInputEvent event) {
    }

    public boolean isLooping() {
        return player.isLooping();
    }

    public boolean isPaused() {
        return player.isPaused();
    }

    public void pause() {
        player.pause();
    }

    public void reset() {
        player.resetFrame();
    }

    public void resume() {
        player.resume();
    }

    public void setLooping(boolean looping) {
        player.setLooping(looping);
    }

    public void setPaused(boolean paused) {
        player.setPaused(paused);
    }



    public interface OnDone {
        void onDone();
    }
}
