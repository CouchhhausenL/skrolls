package main.java.console;

import main.java.console.factory.TerminalHolder;
import main.java.console.input.InputEvent;
import main.java.console.input.InputParser;
import main.java.util.ANSI;
import main.java.util.Color;
import main.java.util.closer.AutoCloseableByCloser;
import main.java.util.closer.Closer;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.PrintWriter;

public class Console implements AutoCloseableByCloser {
    private final Terminal terminal;
    private final Closer closer;

    private int width;
    private int height;

    private final PrintWriter writer;
    private final InputParser input;

    private final CellBuffer front;
    private final CellBuffer back;

    public Console(TerminalType terminalType) throws ConsoleConstructionException {
        TerminalHolder holder;
        try {
            holder = terminalType.create();
        } catch (IOException e) {
            throw new ConsoleConstructionException(e);
        }

        terminal = holder.terminal();
        closer = holder.closer();

        writer = terminal.writer();
        input = new InputParser(terminal);

        front = new CellBuffer(0, 0);
        back = new CellBuffer(0, 0);
        updateDimensions();

        if (width == 0 || height == 0) {
            throw new ConsoleConstructionException("Terminal emulator is not supported");
        }

        input.start();
    }

    public void render() {
        for (int y = 0; y < height; y++) {
            int lastWrittenX = -1;
            int lastWrittenForeground = -1;
            int lastWrittenBackground = -1;

            for (int x = 0; x < width; x++) {
                final char frontChar = front.getChar(x, y);
                final int frontForeground = front.getForeground(x, y);
                final int frontBackground = front.getBackground(x, y);

                final char backChar = back.getChar(x, y);
                final int backForeground = back.getForeground(x, y);
                final int backBackground = back.getBackground(x, y);

                if (backChar == CellBuffer.EMPTY_CELL) continue;
                if (
                        frontChar == backChar &&
                                frontForeground == backForeground &&
                                frontBackground == backBackground
                ) continue;

                if (backBackground == CellBuffer.TRANSPARENT_BACKGROUND)
                    throw new IllegalArgumentException("Transparent background is not allowed in direct render");

                front.set(x, y, backChar, backForeground, backBackground);

                if (lastWrittenX == -1 || x != lastWrittenX + 1)
                    setCursor(x, y);
                if (lastWrittenForeground != backForeground)
                    setForeground(backForeground);
                if (lastWrittenBackground != backBackground)
                    setBackground(backBackground);

                writer.print(backChar);

                lastWrittenX = x;
                lastWrittenForeground = backForeground;
                lastWrittenBackground = backBackground;
            }
        }

        terminal.flush();
    }

    public void writeCell(int x, int y, char character, int foreground, int background) {
        back.set(x, y, character, foreground, background);
    }

    public void writeString(int x, int y, String string, int foreground, int background) {
        for (char character : string.toCharArray()) {
            writeCell(x, y, character, foreground, background);
            x++;
        }
    }
    public void writeString(int x, int y, String string, int foreground) {
        writeString(x, y, string, foreground, Color.BLACK);
    }
    public void writeString(int x, int y, String string) {
        writeString(x, y, string, Color.WHITE);
    }

    public void writeBuffer(CellBuffer buffer) {
        if (buffer.getWidth() != width || buffer.getHeight() != height)
            throw new IllegalArgumentException("Buffer dimensions do not match");

        back.overlay(0, 0, buffer);
    }

    public void writeFill(int color) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writeCell(x, y, ' ', 0, color);
            }
        }
    }

    public InputEvent drainEvent() {
        return input.drainEvent();
    }

    private void setCursor(int x, int y) {
        terminal.puts(InfoCmp.Capability.cursor_address, y, x);
    }

    private void setForeground(int color) {
        writer.printf(ANSI.SET_FOREGROUND,
                (color >> 16) & 0xFF,
                (color >> 8) & 0xFF,
                color & 0xFF
        );
    }

    private void setBackground(int color) {
        writer.printf(ANSI.SET_BACKGROUND,
                (color >> 16) & 0xFF,
                (color >> 8) & 0xFF,
                color & 0xFF
        );
    }

    public boolean areDimensionsDirty() {
        return width != terminal.getColumns() || height != terminal.getRows();
    }

    public void updateDimensions() {
        width = terminal.getColumns();
        height = terminal.getRows();

        front.resize(width, height);
        back.resize(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public Closer getCloser() {
        return closer;
    }

    @Override
    public void closeSelf() throws Exception {
        input.stop();
        terminal.close();
    }
}
