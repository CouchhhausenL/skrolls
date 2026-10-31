package main.java.screen.widget.impl;

import main.java.screen.DrawContextView;
import main.java.util.Color;

// Concrete text field with a placeholder and configurable colors
public class TextFieldWidget extends AbstractTextFieldWidget {
    private String placeholder;
    private final Colors colors;

    // background, cursor, placeholder, content
    public static final Colors DEFAULT_COLORS = new Colors(
            Color.BG0,
            Color.BG3,
            Color.BG5,
            Color.WHITE
    );

    public TextFieldWidget(int x, int y, int width, int height, String placeholder, String content, Colors colors) {
        super(x, y, width, height, content);
        this.placeholder = placeholder;
        this.colors = colors;
    }

    public TextFieldWidget(int x, int y, int width, int height, String placeholder, Colors colors) {
        this(x, y, width, height, placeholder, "", colors);
    }

    public TextFieldWidget(int x, int y, int width, int height, Colors colors) {
        this(x, y, width, height, "", colors);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void onDraw(DrawContextView context) {
        context.fill(0, 0, getWidth(), getHeight(), colors.background());
        // Show the placeholder (with a cursor block) while the field is empty
        if (getContent().isEmpty()) {
            context.cell(0, 0, colors.cursor());
            context.drawString(0, 0, placeholder, colors.placeholder());
        } else {
            drawCursor(context, 0, 0, colors.cursor());
            drawVisibleText(context, 0, 0, colors.content());
        }
    }

    public record Colors(int background, int cursor, int placeholder, int content) {
    }
}
