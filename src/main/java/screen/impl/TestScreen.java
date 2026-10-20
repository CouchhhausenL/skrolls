package main.java.screen.impl;

import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.TextFieldWidget;
import main.java.screen.widget.impl.VerbInputWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;

public class TestScreen extends Screen {

    @Override
    public void init(GameContext context) {
        addChild(new VerbInputWidget(context, 1, 1, 40, 6));
    }

    @Override
    public void onResize() {

    }
}
