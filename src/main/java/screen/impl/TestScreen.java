package main.java.screen.impl;

import main.java.screen.Screen;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;

public class TestScreen extends Screen {

    @Override
    public void init() {
        ScrollableContainerWidget containerWidget = new ScrollableContainerWidget(0, 0, 20, 20, 1);
        for (int i = 0; i < 20; i++) {
            containerWidget.addChild(new LabelWidget(0, 0, 10, 1, "Hello " + i));
        }
        addChild(containerWidget);
    }

    @Override
    public void onResize() {

    }
}
