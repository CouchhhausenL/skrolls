package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.util.SkaParser;

// Plays the transition animation, then enters the battle screen.
// Can be skipped early with [F].
public class BattleScreenTransition extends Screen {
    private AnimationWidget.OnDone endTransition;
    private LabelWidget skipHintWidget;


    @Override
    protected void init(GameContext context) {
        // Fires either when the animation finishes or when the user skips it
        endTransition = () -> {
            context.setScreen(new BattleScreen());
        };

        Animation transitionAnimation = SkaParser.parse("utility/battle_transition.ska");
        AnimationWidget widget = new AnimationWidget(0, 0, transitionAnimation);
        autoPositionWidget(widget);
        skipHintWidget = new LabelWidget(0, 0, "Press [F] to skip");
        skipHintWidget.setPosition(1, getHeight() - 2);
        addChild(widget);
        addChild(skipHintWidget);
        widget.onDone(endTransition);

    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isChar(KeyInputEvent.Letter.F))
            endTransition.onDone();
    }

    @Override
    public void onResize() {

    }


}