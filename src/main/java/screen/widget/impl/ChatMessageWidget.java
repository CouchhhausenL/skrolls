package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.util.Color;

import java.util.ArrayList;
import java.util.List;

// Word-wrapping message line. The widget grows in height to fit the wrapped
// text, splitting words that are longer than the available width.
public class ChatMessageWidget extends Widget {
    private final String content;
    private List<String> lines = new ArrayList<>();

    public ChatMessageWidget(int x, int y, int width, String content) {
        super(x, y, width, 1);
        this.content = content;
        recalculateLines();
    }

    private void recalculateLines() {
        int width = getWidth();

        if (width <= 0) {
            lines = new ArrayList<>();
            setHeight(1);
            return;
        }

        List<String> newLines = new ArrayList<>();

        // keep \n
        // -1 preserves trailing empty paragraphs so blank lines survive
        String[] paragraphs = content.split("\n", -1);

        for (String paragraph : paragraphs) {
            String[] words = paragraph.split(" ");

            StringBuilder line = new StringBuilder();

            for (String word : words) {
                // split words that do not fit
                while (word.length() > width) {
                    if (!line.isEmpty()) {
                        newLines.add(line.toString());
                        line.setLength(0);
                    }

                    newLines.add(word.substring(0, width));
                    word = word.substring(width);
                }

                // add it normally if it fits
                if (line.isEmpty()) {
                    line.append(word);
                } else if (line.length() + 1 + word.length() <= width) {
                    line.append(" ").append(word);
                } else {
                    newLines.add(line.toString());
                    line.setLength(0);
                    line.append(word);
                }
            }

            // keep \n as blank lines
            if (!line.isEmpty()) {
                newLines.add(line.toString());
            } else if (paragraph.isEmpty()) {
                newLines.add("");
            }
        }

        this.lines = newLines;
        // Final height matches the number of wrapped lines
        setHeight(Math.max(1, lines.size()));
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        recalculateLines();
    }

    @Override
    public void setDimensions(int width, int height) {
        super.setWidth(width);
        recalculateLines();
    }

    @Override
    protected void onDraw(DrawContextView context) {
        for (int i = 0; i < lines.size(); i++) {
            context.drawString(0, i, lines.get(i), Color.WHITE);
        }
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {

    }

    @Override
    public void onMouseInput(MouseInputEvent event) {

    }
}
