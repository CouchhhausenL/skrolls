package main.java.screen;

import main.java.console.input.InputEvent;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.game.GameContext;
import main.java.screen.widget.Widget;
import main.java.screen.widget.impl.AnimationWidget;

import java.util.ArrayList;
import java.util.List;

// A full-screen view made of widgets. Subclasses populate widgets in init()
// and receive input/resize callbacks; drawing and input dispatch are final.
public abstract class Screen {
    private final List<Widget> widgets = new ArrayList<>();
    private int width = 0;
    private int height = 0;
    private boolean initialized = false;

    public Screen() {
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

    public final void tryInit(GameContext context) {
        if (initialized) return;
        initialized = true;
        init(context);
    }

    protected abstract void init(GameContext context);

    public void addChild(Widget widget) {
        widgets.add(widget);
    }

    public void removeChild(Widget widget) {
        widgets.remove(widget);
    }

    public final void draw(DrawContext context) {
        onPreDraw(context);
        // Snapshot the list because widgets may add/remove themselves while drawing
        for (Widget widget : new ArrayList<>(widgets)) {
            widget.draw(context);
        }
        // Every pushed scissor must have been popped by its widget
        context.throwIfScissorStackInvalid();
        context.throwIfLimitStackInvalid();
        onPostDraw(context);
    }

    public final void onInputEvent(InputEvent event) {
        if (event instanceof KeyInputEvent keyInputEvent) {
            onKeyEvent(keyInputEvent);
            distributeKeyInputEvent(keyInputEvent, widgets);
        }
        if (event instanceof MouseInputEvent mouseInputEvent) {
            onMouseEvent(mouseInputEvent);
            distributeMouseInputEvent(mouseInputEvent, widgets);
        }
    }

    public void onKeyEvent(KeyInputEvent event) {
    }

    public void onMouseEvent(MouseInputEvent event) {
    }

    public static void distributeKeyInputEvent(KeyInputEvent event, List<Widget> widgets) {
        for (Widget widget : new ArrayList<>(widgets)) {
            widget.onKeyInput(event);
        }
    }

    public static void distributeMouseInputEvent(MouseInputEvent event, List<Widget> widgets) {
        MouseInputEvent.Type type = event.type();
        int x = event.x();
        int y = event.y();
        int button = event.button();

        List<Widget> snapshot = new ArrayList<>(widgets);
        // Iterate in reverse order to make checking for topmost hovered widget easier
        boolean hoveredFound = false;
        for (int i = snapshot.size() - 1; i >= 0; i--) {
            Widget widget = snapshot.get(i);
            // Skip widgets whose bounds do not contain the cursor
            if (!(
                    x >= widget.getX() && x < widget.getX() + widget.getWidth() &&
                            y >= widget.getY() && y < widget.getY() + widget.getHeight()
            )) {
                widget.setHovered(false);
                continue;
            }

            // The first (topmost) widget under the cursor is the hovered one
            if (!hoveredFound) {
                hoveredFound = true;
                widget.setHovered(true);
            } else {
                widget.setHovered(false);
            }

            // Rebase the event coordinates to widget-local space
            widget.onMouseInput(new MouseInputEvent(
                    type,
                    x - widget.getX(),
                    y - widget.getY(),
                    button)
            );
        }
    }

    public abstract void onResize();

    // Centers a widget on the screen
    protected void autoPositionWidget(Widget widget) {
        final int width = widget.getWidth();
        final int height = widget.getHeight();
        final int screenWidth = getWidth();
        final int screenHeight = getHeight();

        widget.setPosition(
                (screenWidth - width) / 2,
                (screenHeight - height) / 2
        );

    }

    protected void onPreDraw(DrawContext context) {
    }

    protected void onPostDraw(DrawContext context) {
    }
}
