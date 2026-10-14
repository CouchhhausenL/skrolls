package main.java.screen.widget.impl;

import main.java.animation.Animation;
import main.java.animation.AnimationPlayer;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;

public class AnimationWidget extends Widget {
    private final AnimationPlayer player;

    public AnimationWidget(int x, int y, int width, int height, Animation animation) {
        super(x, y, width, height);
        player = new AnimationPlayer(animation);
    }

    @Override
    public void onDraw(DrawContextView context) {
        player.update();
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
}
