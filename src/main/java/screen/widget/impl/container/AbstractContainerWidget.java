package main.java.screen.widget.impl.container;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContext;
import main.java.screen.DrawContextView;
import main.java.screen.Screen;
import main.java.screen.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerWidget extends Widget {
    protected List<Widget> widgets = new ArrayList<Widget>();

    public AbstractContainerWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void addChild(Widget widget) {
        widgets.add(widget);
    }

    public void removeChild(Widget widget) {
        widgets.remove(widget);
    }

    public void clearChildren() {
        widgets.clear();
    }

    @Override
    public void draw(DrawContext context) {
        super.draw(context);
        updateWidgetPositions();
        context.pushScissor(0, 0, getWidth(), getHeight());
        for (Widget widget : widgets) {
            widget.draw(context);
        }
        context.popScissor();
        onPostDraw(context);
    }

    @Override
    public void onDraw(DrawContextView context) {

    }

    protected void onPostDraw(DrawContextView context) {

    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        Screen.distributeKeyInputEvent(event, widgets);
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
        Screen.distributeMouseInputEvent(event, widgets);
    }

    protected abstract void updateWidgetPositions();
}
