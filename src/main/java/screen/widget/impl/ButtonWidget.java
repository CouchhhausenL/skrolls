package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;

public class ButtonWidget extends Widget {
    private String text;
    private OnClick onClick = null;

    public ButtonWidget(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    public ButtonWidget onClick(OnClick onClick) {
        this.onClick = onClick;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public void onDraw(DrawContextView context) {
        context.fill(0, 0, getWidth(), getHeight(), isHovered() ? 0xFFFFFF : 0xB0B0B0);
        context.drawString(0, getHeight() / 2, text, 0);
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {

    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
        if (event.type() != MouseInputEvent.Type.RELEASE) return;
        if (onClick != null) onClick.onClick();
    }

    public interface OnClick {
        void onClick();
    }
}
