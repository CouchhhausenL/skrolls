package main.java.screen.impl;

import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.util.SkaParser;

public class CoverScreen extends Screen {
    AnimationWidget animationWidget;
    private GameContext context;

    @Override
    protected void init(GameContext context) {
        this.context = context;
        animationWidget = new AnimationWidget(0, 0, SkaParser.parse("cover.ska"));
        addChild(animationWidget);
    }

    @Override
    public void onResize() {

    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isEscape()) {
            context.setScreen(new MainGameScreen());
        }
    }
}
