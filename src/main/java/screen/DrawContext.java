package main.java.screen;

import main.java.console.CellBuffer;
import main.java.console.Console;

import java.util.Stack;

public class DrawContext implements DrawContextView {
    private final CellBuffer buffer;
    private final ScissorStack scissorStack = new ScissorStack();

    private int offsetX = 0;
    private int offsetY = 0;
    private int width = 0;
    private int height = 0;

    public DrawContext(int width, int height) {
        this.buffer = new CellBuffer(width, height);
    }

    public void writeToConsole(Console console) {
        console.writeBuffer(buffer);
    }

    public void clear() {
        buffer.clear();
    }
    public void resize(int width, int height) {
        buffer.resize(width, height);
    }

    public void setLimits(int offsetX, int offsetY, int width, int height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }
    public void clearLimits() {
        this.offsetX = 0;
        this.offsetY = 0;
        this.width = 0;
        this.height = 0;
    }

    public void throwIfScissorStackInvalid() {
        if (scissorStack.size() > 0) throw new IllegalStateException("ScissorStack is not empty");
    }

    @Override
    public void cell(int x, int y, char character, int foreground, int background) {
        if (width != 0 && height != 0) {
            // >= because 0 is also inside
            if (x >= width || y >= height) return;
        }
        x += offsetX;
        y += offsetY;
        if (scissorStack.isCutOff(x, y)) return;

        buffer.setTransparent(x, y, character, foreground, background);
    }
    @Override
    public void cell(int x, int y, int background) {
        cell(x, y, ' ', 0, background);
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, char character, int foreground, int background) {
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                cell(x, y, character, foreground, background);
            }
        }
    }
    @Override
    public void fill(int x1, int y1, int x2, int y2, int background) {
        fill(x1, y1, x2, y2, ' ', 0, background);
    }

    @Override
    public void drawString(int x, int y, String string, int foreground, int background) {
        for (int i = 0; i < string.length(); i++) {
            cell(x + i, y, string.charAt(i), foreground, background);
        }
    }
    @Override
    public void drawString(int x, int y, String string, int foreground) {
        drawString(x, y, string, foreground, CellBuffer.TRANSPARENT_BACKGROUND);
    }

    @Override
    public void pushScissor(int x1, int y1, int x2, int y2) {
        scissorStack.push(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);
    }
    @Override
    public void popScissor() {
        scissorStack.pop();
    }

    static class ScissorStack {
        private final Stack<Entry> stack =  new Stack<>();

        private ScissorStack() {

        }

        public void push(int x1, int y1, int x2, int y2) {
            stack.push(new Entry(x1, y1, x2, y2));
        }
        public void pop() {
            stack.pop();
        }
        public int size() {
            return stack.size();
        }

        public boolean isCutOff(int x, int y) {
            if (stack.size() == 0) return false;

            for (Entry entry : stack) {
                if (x >= entry.x1 && x <= entry.x2 && y >= entry.y1 && y <= entry.y2) return true;
            }

            return false;
        }

        private record Entry(int x1, int y1, int x2, int y2) {

        }
    }
}
