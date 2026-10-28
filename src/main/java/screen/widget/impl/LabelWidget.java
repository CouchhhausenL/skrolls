package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.util.Color;

// Multi-line text label. In "fancy" mode the text types itself out one
// character at a time, using displayedCharCount as the reveal progress.
public class LabelWidget extends Widget {
    private String content;
    private boolean fancyMode;

    private int color = Color.WHITE;

    private int displayedCharCount = 0;
    private long lastTime = 0;
    private long fancyCharDelayTime = 100;

    public LabelWidget(int x, int y, int width, int height, String content) {
        super(x, y, width, height);
        this.content = content;
    }
    public LabelWidget(String content, int color){
        this(0,0,calculateWidth(content),calculateHeight(content),content);
        this.color = color;
    }
    public LabelWidget(String content){
        this(0,0,calculateWidth(content),calculateHeight(content),content);
    }
    public LabelWidget(int x, int y, String content) {
        this(x, y, calculateWidth(content), calculateHeight(content), content);
    }
    public LabelWidget(int x, int y, String content, int color){
        this(x,y,calculateWidth(content), calculateHeight(content), content);
        this.color = color;
    };

    public static int calculateWidth(String text) {
        int max = 0;
        for (String line : text.split("\n", -1)) {
            max = Math.max(max, line.length());
        }
        return Math.max(1, max);
    }

    public static int calculateHeight(String text) {
        return Math.max(1, text.split("\n", -1).length);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        setDimensions(calculateWidth(content), calculateHeight(content));
        this.displayedCharCount = 0;
    }

    public int getColor() {
        return color;
    }

    public void setFancyMode(boolean fancyMode, int fancyCharDelayTime) {
        this.fancyMode = fancyMode;
        this.fancyCharDelayTime = fancyCharDelayTime;
    }

    @Override
    public void onDraw(DrawContextView context) {
        // Reveal at most one extra character once the delay has elapsed
        if (fancyMode) {
            long now = System.currentTimeMillis();
            if (now - lastTime > fancyCharDelayTime && displayedCharCount < content.length()) {
                displayedCharCount++;
                lastTime = now;
            }
        }

        String textToDraw = fancyMode ? content.substring(0, displayedCharCount) : content;

        // Split on \n (keeping trailing empties) and draw line by line
        String[] lines = textToDraw.split("\n", -1);
        int y = 0;
        for (String line : lines) {
            context.drawString(0, y, line, color);
            y++;
        }
    }


    @Override
    public void onKeyInput(KeyInputEvent event) {

    }

    @Override
    public void onMouseInput(MouseInputEvent event) {

    }
}
