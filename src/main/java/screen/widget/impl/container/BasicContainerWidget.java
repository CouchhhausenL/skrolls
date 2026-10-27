package main.java.screen.widget.impl.container;

import main.java.screen.widget.Widget;

// Basic container that preserves the manual positions (x, y) of its children
// without applying an automatic layout strategy.
public class BasicContainerWidget extends AbstractContainerWidget {

    public BasicContainerWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    // Helper method to add a child and set its relative position directly
    public void addChild(Widget widget, int x, int y) {
        widget.setPosition(x, y);
        addChild(widget);
    }

    @Override
    protected void updateWidgetPositions() {
        // No automatic positioning: children keep their manually assigned coordinates
    }
}
