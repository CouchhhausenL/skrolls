package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.util.Color;

public class LabelWidget extends Widget {
    private String content;

    public LabelWidget(int x, int y, int width, int height, String content) {
        super(x, y, width, height);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void draw(DrawContextView context) {
        context.drawString(0, 0, content, isHovered() ? Color.RED : Color.WHITE);
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {

    }

    @Override
    public void onMouseInput(MouseInputEvent event) {

    }
}
