package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;

public class BoxWidget extends Widget {
    private final int color;

    public BoxWidget(int x, int y, int width, int height, int color) {
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public void onDraw(DrawContextView context) {
        int w = getWidth();
        int h = getHeight();
        if (w < 2 || h < 2) return;

        String border = "+" + "-".repeat(w - 2) + "+";
        context.drawString(0, 0, border, color);
        for (int y = 1; y < h - 1; y++) {
            context.drawString(0, y, "|", color);
            context.drawString(w - 1, y, "|", color);
        }
        context.drawString(0, h - 1, border, color);
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
    }
}