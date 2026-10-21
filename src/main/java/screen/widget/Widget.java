package main.java.screen.widget;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContext;
import main.java.screen.DrawContextView;

// Base UI element with a position/size. draw() installs the widget's
// rectangle as the draw limit so subclasses work in local coordinates.
public abstract class Widget {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean hovered = false;

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        if (width <= 0) throw new IllegalArgumentException("width out of range");
        this.width = width;
    }

    public void setHeight(int height) {
        if (height <= 0) throw new IllegalArgumentException("height out of range");
        this.height = height;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public void setDimensions(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    // Limits translate drawing into this widget's space and also clip its size
    public void draw(DrawContext context) {
        try {
            context.pushLimits(getX(), getY(), getWidth(), getHeight());
            onDraw(context);
        } finally {
            context.popLimits();
        }
    }

    protected abstract void onDraw(DrawContextView context);

    public abstract void onKeyInput(KeyInputEvent event);

    public abstract void onMouseInput(MouseInputEvent event);

}
