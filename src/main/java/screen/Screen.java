package main.java.screen;

import main.java.console.input.InputEvent;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    private final List<Widget> widgets = new ArrayList<>();
    private int width = 0;
    private int height = 0;

    public Screen() {
        init();
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public abstract void init();

    public void addChild(Widget widget) {
        widgets.add(widget);
    }
    public void removeChild(Widget widget) {
        widgets.remove(widget);
    }

    public final void draw(DrawContext context) {
        onPreDraw(context);
        for (Widget widget : widgets) {
            try {
                context.setLimits(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
                widget.draw(context);
                context.throwIfScissorStackInvalid();
            } finally {
                context.clearLimits();
            }
        }
        onPostDraw(context);
    }
    public void onInputEvent(InputEvent event) {
        // Iterate in reverse order to make checking for topmost hovered widget easier

        boolean hoveredFound = false;
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (event instanceof MouseInputEvent(MouseInputEvent.Type type, int x, int y, int button)) {
                if (!(
                        x >= widget.getX() && x < widget.getX() + widget.getWidth() &&
                        y >= widget.getY() && y < widget.getY() + widget.getHeight()
                )) {
                    widget.setHovered(false);
                    continue;
                }

                if (!hoveredFound) {
                    hoveredFound = true;
                    widget.setHovered(true);
                } else {
                    widget.setHovered(false);
                }

                widget.onMouseInput(new MouseInputEvent(
                        type,
                        x - widget.getX(),
                        y - widget.getY(),
                        button)
                );
                continue;
            }
            if (event instanceof KeyInputEvent keyInputEvent)
                widget.onKeyInput(keyInputEvent);
        }
    }

    public abstract void onResize();
    protected void onPreDraw(DrawContext context) {}
    protected void onPostDraw(DrawContext context) {}
}
