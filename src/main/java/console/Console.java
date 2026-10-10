package main.java.console;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.io.PrintWriter;

public class Console implements AutoCloseable {
    private final Terminal terminal;
    private int width;
    private int height;

    private final PrintWriter writer;
    private final NonBlockingReader reader;

    private final CellBuffer front;
    private final CellBuffer back;

    public Console() throws IOException {
        terminal = TerminalBuilder.terminal();
        front = new CellBuffer(width, height);
        back = new CellBuffer(width, height);
        updateDimensions();

        writer = terminal.writer();
        reader = terminal.reader();

        terminal.enterRawMode();
        if (terminal.hasMouseSupport())
            terminal.trackMouse(Terminal.MouseTracking.Any);
    }

    public void loop() {
        //TODO: Add looping functionality
    }

    private boolean areDimensionsDirty() {
        return width != terminal.getColumns() || height != terminal.getRows();
    }

    private void updateDimensions() {
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
    public void close() throws IOException {
        terminal.close();
    }
}
