package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;

// Single-line editable text: cursor movement, backspace, character insertion
// and a horizontally scrolled viewport when the text is wider than the widget.
public abstract class AbstractTextFieldWidget extends Widget {
    private String content;
    protected int cursor;

    public AbstractTextFieldWidget(int x, int y, int width, int height, String content) {
        super(x, y, width, height);
        this.content = content;
    }

    public AbstractTextFieldWidget(int x, int y, int width, int height) {
        this(x, y, width, height, "");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Draws the portion of the text that fits, scrolled so the cursor is visible
    protected void drawVisibleText(DrawContextView context, int x, int y, int color) {
        int drawStart = getDrawStart();
        String visible = content.substring(drawStart, Math.min(content.length(), drawStart + getWidth()));
        context.drawString(x, y, visible, color);
    }

    protected void drawCursor(DrawContextView context, int x, int y, int color) {
        context.cell(cursor - getDrawStart() + x, y, color);
    }

    // First visible character index; scrolls right once the cursor passes the width
    private int getDrawStart() {
        return Math.max(0, cursor - getWidth() + 1);
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (event.isArrow(KeyInputEvent.Direction.LEFT))
            cursor = Math.max(0, cursor - 1);
        if (event.isArrow(KeyInputEvent.Direction.RIGHT))
            cursor = Math.min(cursor + 1, content.length());

        // Delete the character before the cursor and shift it left
        if (event.isBackspace() && cursor > 0) {
            setContent(
                    content.substring(0, cursor - 1) +
                            content.substring(cursor)
            );
            cursor--;
            return;
        }

        // Insert the typed character at the cursor
        if (event.isChar()) {
            setContent(
                    content.substring(0, cursor) +
                            event.asChar() +
                            content.substring(cursor)
            );
            cursor++;
        }
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
    }
}
