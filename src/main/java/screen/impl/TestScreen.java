package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.screen.DrawContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.ButtonWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.TextFieldWidget;
import main.java.util.Color;
import main.java.util.SkaParser;

import java.io.IOException;

public class TestScreen extends Screen {

    LabelWidget label;
    boolean magenta = false;

    @Override
    public void init() {
        label = new LabelWidget(2, 1, 20, 1, "Resize to see...");
        addChild(label);

        addChild(new TextFieldWidget(2, 4, 20, 1, "Enter test"));

        addChild(new ButtonWidget(2, 8, 10, 5, "Click me!").onClick(() -> {
            try {
                Animation animation = SkaParser.parse("title_screen.ska");
                animation.getFrame(0).toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public void onResize() {
        label.setContent(getWidth() + " x " + getHeight());
    }

    @Override
    protected void onPreDraw(DrawContext context) {
        if (!magenta) return;
        context.fill(0, 0, getWidth(), getHeight(), Color.MAGENTA);
    }
}
