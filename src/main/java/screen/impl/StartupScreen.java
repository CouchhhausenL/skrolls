package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.util.SkaParser;

public class StartupScreen extends Screen {
    private AnimationWidget animationWidget;

    @Override
    protected void init(GameContext context) {
        Animation animation = SkaParser.parse("title_screen.ska");
        animationWidget = new AnimationWidget(0, 0, animation.getWidth(), animation.getHeight(), animation);
        animationWidget.onDone(() -> {
            context.setScreen(new TestScreen());
        });
        positionAnimation();
        addChild(animationWidget);
    }

    private void positionAnimation() {
        final int width = animationWidget.getWidth();
        final int height = animationWidget.getHeight();
        final int screenWidth = getWidth();
        final int screenHeight = getHeight();

        animationWidget.setPosition(
                (screenWidth - width) / 2,
                (screenHeight - height) / 2
        );
    }

    @Override
    public void onResize() {
        positionAnimation();
    }
}
