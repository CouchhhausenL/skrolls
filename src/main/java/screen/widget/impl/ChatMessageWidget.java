package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.util.Color;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageWidget extends Widget {
    private final String content;

    public ChatMessageWidget(int x, int y, int width, String content) {
        super(x, y, width, 1);
        this.content = content;
    }

    @Override
    protected void onDraw(DrawContextView context) {
        int width = getWidth();

        if (width <= 0) {
            setHeight(0);
            return;
        }

        List<String> lines = new ArrayList<>();

        // keep \n
        String[] paragraphs = content.split("\n", -1);

        for (String paragraph : paragraphs) {
            String[] words = paragraph.split(" ");

            StringBuilder line = new StringBuilder();

            for (String word : words) {
                // split words that do not fit
                while (word.length() > width) {
                    if (!line.isEmpty()) {
                        lines.add(line.toString());
                        line.setLength(0);
                    }

                    lines.add(word.substring(0, width));
                    word = word.substring(width);
                }

                // add it normally if it fits
                if (line.isEmpty()) {
                    line.append(word);
                } else if (line.length() + 1 + word.length() <= width) {
                    line.append(" ").append(word);
                } else {
                    lines.add(line.toString());
                    line.setLength(0);
                    line.append(word);
                }
            }

            // keep \n as blank lines
            if (!line.isEmpty()) {
                lines.add(line.toString());
            } else if (paragraph.isEmpty()) {
                lines.add("");
            }
        }

        setHeight(lines.size());

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
