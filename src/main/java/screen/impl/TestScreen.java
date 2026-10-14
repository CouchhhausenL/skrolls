package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.ButtonWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.TextFieldWidget;
import main.java.util.SkaParser;

import java.io.IOException;

public class TestScreen extends Screen {

    LabelWidget label;

    @Override
    public void init() {
        Animation animation;
        try {
            animation = SkaParser.parse("map/map3.ska");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AnimationWidget widget = new AnimationWidget(0, 0, 100, 50, animation);
        widget.setLooping(true);
        addChild(widget);

        label = new LabelWidget(2, 1, 20, 1, "Resize to see...");
        addChild(label);

        addChild(new TextFieldWidget(2, 4, 20, 1, "Enter test"));

        addChild(new ButtonWidget(2, 8, 10, 5, "Click me!").onClick(() -> widget.setPaused(!widget.isPaused())));
    }

    @Override
    public void onResize() {
        label.setContent(getWidth() + " x " + getHeight());
    }
}
