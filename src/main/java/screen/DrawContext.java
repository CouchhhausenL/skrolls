package main.java.screen;

import main.java.console.CellBuffer;
import main.java.console.Console;

import java.util.Stack;

// Drawing surface for a frame. Supports a translation+size limit (set by
// Widget.draw) and a stack of scissor rectangles for clipped containers.
// All coordinates are relative to that limit.
public class DrawContext implements DrawContextView {
    private final CellBuffer buffer;
    private final ScissorStack scissorStack = new ScissorStack();
    private final Stack<LimitEntry> limitStack = new Stack<>();

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
        limitStack.clear();
        clearLimits();
    }

    public void resize(int width, int height) {
        buffer.resize(width, height);
    }

    public void pushLimits(int x, int y, int width, int height) {
        limitStack.push(new LimitEntry(this.offsetX, this.offsetY, this.width, this.height));
        this.offsetX += x;
        this.offsetY += y;
        this.width = width;
        this.height = height;
    }

    public void popLimits() {
        if (limitStack.isEmpty()) {
            throw new IllegalStateException("LimitStack is empty");
        }
        LimitEntry previous = limitStack.pop();
        this.offsetX = previous.offsetX();
        this.offsetY = previous.offsetY();
        this.width = previous.width();
        this.height = previous.height();
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

    public void throwIfLimitStackInvalid() {
        if (!limitStack.isEmpty()) throw new IllegalStateException("LimitStack is not empty");
    }

    // All drawing funnels through here so limits, translation, scissor and
    // buffer bounds are enforced in one place
    @Override
    public void cell(int x, int y, char character, int foreground, int background) {
        // Reject coordinates outside the current widget's local size (0 disables)
        if (width != 0 && height != 0) {
            // >= because 0 is also inside
            if (x < 0 || y < 0 || x >= width || y >= height) return;
        }

        // Widget-local -> screen coordinates
        x += offsetX;
        y += offsetY;
        if (scissorStack.isCutOff(x, y)) return;

        if (x < 0 || y < 0) return;
        if (x >= buffer.getWidth() || y >= buffer.getHeight()) return;
        buffer.setTransparent(x, y, character, foreground, background);
    }

    @Override
    public void cell(int x, int y, int background) {
        cell(x, y, ' ', 0, background);
    }

    // Half-open rectangle: x1/y1 inclusive, x2/y2 exclusive
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

    // Blits a buffer at an offset, skipping empty cells
    @Override
    public void drawBuffer(int rootX, int rootY, CellBuffer buffer) {
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                char character = buffer.getCharacter(x, y);
                if (character == CellBuffer.EMPTY_CELL) continue;
                int foreground = buffer.getForeground(x, y);
                int background = buffer.getBackground(x, y);

                cell(x + rootX, y + rootY, character, foreground, background);
            }
        }
    }

    // Scissor rectangles are stored in screen space, so the offset is applied here
    @Override
    public void pushScissor(int x1, int y1, int x2, int y2) {
        scissorStack.push(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);
    }

    @Override
    public void popScissor() {
        scissorStack.pop();
    }

    private record LimitEntry(int offsetX, int offsetY, int width, int height) {
    }

    static class ScissorStack {
        private final Stack<Entry> stack = new Stack<>();

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

        // Clipped if the point lies outside any active scissor rectangle
        // (rectangles are intersected by requiring all of them to contain it)
        public boolean isCutOff(int x, int y) {
            if (stack.isEmpty()) return false;

            for (Entry entry : stack) {
                if (!(x >= entry.x1 && x <= entry.x2 && y >= entry.y1 && y <= entry.y2)) return true;
            }

            return false;
        }

        private record Entry(int x1, int y1, int x2, int y2) {

        }
    }
}
