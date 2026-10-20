package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.util.Color;
import main.java.util.SkaParser;

import java.util.LinkedHashMap;

// Title screen with the animated logo and Start/Exit options
public class MainMenuScreen extends Screen {
    private OptionsWidget optionsWidget;

    @Override
    protected void init(GameContext context) {
        Animation animation = SkaParser.parse("title.ska");
        addChild(new AnimationWidget(3, 2, animation));

        optionsWidget = new OptionsWidget(2, animation.getHeight() + 4, 80, 20, Color.WHITE, new LinkedHashMap<>() {
            {
                put("Start Game", () -> context.setScreen(new IntroductionScreen()));
                put("Exit Game", context::stop);
            }
        });

        addChild(optionsWidget);
        addChild(new LabelWidget(3, 11 + optionsWidget.getHeight() + 2, "Press [SPACE] to select"));
    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {

    }

    @Override
    public void onMouseEvent(MouseInputEvent event) {

    }

    @Override
    public void onResize() {

    }
}
