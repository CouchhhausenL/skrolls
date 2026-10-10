package main.java.console;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class Console {
    private final Terminal terminal;
    private int width;
    private int height;
    private final CellBuffer front;
    private final CellBuffer back;

    public Console() throws IOException {
        terminal = TerminalBuilder.terminal();
        front = new CellBuffer(width, height);
        back = new CellBuffer(width, height);
        updateDimensions();

        terminal.enterRawMode();
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

    public void writeString(String content) {
        writer().println(content);
    }

    public PrintWriter writer() {
        return terminal.writer();
    }

    public Reader reader() {
        return terminal.reader();
    }

    public void flush() {
        terminal.flush();
    }
}
