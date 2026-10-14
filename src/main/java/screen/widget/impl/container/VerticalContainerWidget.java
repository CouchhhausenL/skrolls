package main.java.screen.widget.impl.container;

import main.java.screen.widget.Widget;

public class VerticalContainerWidget extends AbstractContainerWidget {
    protected int gap;

    public VerticalContainerWidget(int x, int y, int width, int height, int gap) {
        super(x, y, width, height);
        this.gap = gap;
    }

    public int getGap() {
        return gap;
    }
    public void setGap(int gap) {
        if (gap < 0) throw new IllegalArgumentException("Gap cannot be negative");
        this.gap = gap;
    }

    @Override
    protected void updateWidgetPositions() {
        int y = 0;
        for (Widget widget : widgets) {
            widget.setPosition(0, y);
            y += widget.getHeight();
            y += gap;
        }
    }
}
