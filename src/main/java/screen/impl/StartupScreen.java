package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.util.SkaParser;

public class StartupScreen extends Screen {
    private AnimationWidget animationWidget;
    private LabelWidget skipHintWidget;
    private AnimationWidget.OnDone endStartup;

    @Override
    protected void init(GameContext context) {
        endStartup = () -> {
            context.setScreen(new TestScreen());
        };

        Animation animation = SkaParser.parse("title_screen.ska");
        animationWidget = new AnimationWidget(0, 0, animation.getWidth(), animation.getHeight(), animation);
        animationWidget.onDone((AnimationWidget.OnDone) endStartup);

        skipHintWidget = new LabelWidget(0, 0, "Press [F] to skip");

        positionWidgets();
        addChild(animationWidget);
        addChild(skipHintWidget);
    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isChar(KeyInputEvent.Letter.F))
            endStartup.onDone();
    }

    private void positionWidgets() {
        final int width = animationWidget.getWidth();
        final int height = animationWidget.getHeight();
        final int screenWidth = getWidth();
        final int screenHeight = getHeight();

        animationWidget.setPosition(
                (screenWidth - width) / 2,
                (screenHeight - height) / 2
        );

        skipHintWidget.setPosition(1, screenHeight - 2);
    }

    @Override
    public void onResize() {
        positionWidgets();
    }
}
