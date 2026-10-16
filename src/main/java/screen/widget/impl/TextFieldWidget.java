package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.util.Color;

public class TextFieldWidget extends Widget {
    private String content;
    private String placeholder;

    public TextFieldWidget(int x, int y, int width, int height, String placeholder, String content) {
        super(x, y, width, height);
        this.placeholder = placeholder;
        this.content = content;
    }

    public TextFieldWidget(int x, int y, int width, int height, String placeholder) {
        this(x, y, width, height, placeholder, "");
    }

    public TextFieldWidget(int x, int y, int width, int height) {
        this(x, y, width, height, "");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void onDraw(DrawContextView context) {
        if (content.isEmpty()) {
            context.drawString(0, 0, placeholder, 0x909090);
        } else {
            context.drawString(0, 0, content, Color.WHITE);
        }
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (!isHovered()) return;
        if (event.isBackspace()) {
            if (content.length() <= 1) {
                content = "";
                return;
            }
            content = content.substring(0, content.length() - 1);
            return;
        }
        if (!event.isChar()) return;
        content += event.asChar();
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {

    }
}
