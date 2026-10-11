package main.java.console;

import main.java.util.ANSI;
import main.java.util.closer.Closer;
import org.jline.builtins.SwingTerminal;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

public enum TerminalType {
    CONSOLE,
    WINDOW;

    public Holder create() throws IOException {
        return switch (this) {
            case CONSOLE -> createNormalTerminal();
            case WINDOW -> createSwingTerminal();
        };
    }

    private static void commonTerminalInit(Terminal terminal) {
        terminal.puts(InfoCmp.Capability.cursor_invisible);
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.puts(InfoCmp.Capability.cursor_home);
        terminal.puts(InfoCmp.Capability.enter_ca_mode); // Alternate screen mode

        terminal.enterRawMode();
        if (terminal.hasMouseSupport()) {
            terminal.trackMouse(Terminal.MouseTracking.Any);
            terminal.writer().write(ANSI.ENABLE_SGR);
        }

        terminal.flush();
    }

    private static void commonTerminalBeforeClose(Terminal terminal) {
        terminal.trackMouse(Terminal.MouseTracking.Off);
        terminal.writer().write(ANSI.DISABLE_SGR);

        terminal.puts(InfoCmp.Capability.exit_ca_mode);
        terminal.puts(InfoCmp.Capability.cursor_visible);

        terminal.flush();
    }

    private static Holder createNormalTerminal() throws IOException {
        Terminal terminal = TerminalBuilder.terminal();

        commonTerminalInit(terminal);

        return new Holder(terminal, () -> {
            commonTerminalBeforeClose(terminal);
        });
    }

    private static Holder createSwingTerminal() throws IOException {
        SwingTerminal terminal = new SwingTerminal();
        commonTerminalInit(terminal);

        SwingTerminal.TerminalComponent component = terminal.getComponent();

        component.setTerminalFont(
                loadTerminalFont()
        );

        try {
            setCursorInvisible(component);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        FontMetrics fm = component.getFontMetrics(
                component.getTerminalFont()
        );

        int cellWidth = fm.charWidth('M');
        int cellHeight = fm.getHeight();

        component.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (cellWidth <= 0 || cellHeight <= 0) {
                    return;
                }

                int columns = component.getWidth() / cellWidth;
                int rows = component.getHeight() / cellHeight;

                terminal.setSize(Size.of(columns, rows));
            }
        });

        JFrame frame = terminal.createFrame("Terminal");
        frame.setVisible(true);

        component.repaint();
        return new Holder(terminal, () -> {
            commonTerminalBeforeClose(terminal);
            component.dispose();
            frame.dispose();
        });
    }

    // Uses java reflection to hide blinking cursor in swing emulator
    public static void setCursorInvisible(SwingTerminal.TerminalComponent component) throws NoSuchFieldException, IllegalAccessException {
        Class<? extends SwingTerminal.TerminalComponent> clazz = component.getClass();

        Field timerField = clazz.getDeclaredField("cursorTimer");
        timerField.setAccessible(true);

        Timer timer = (Timer) timerField.get(component);
        timer.stop();

        Field cursorVisibleField = clazz.getDeclaredField("cursorVisible");
        cursorVisibleField.setAccessible(true);

        AtomicBoolean cursorVisible = (AtomicBoolean) cursorVisibleField.get(component);
        cursorVisible.set(false);

        component.repaint();
    }

    private static Font loadTerminalFont() throws IOException {
        try (InputStream in = TerminalType.class.getResourceAsStream(
                "/assets/font/JetBrainsMono-Regular.ttf")) {

            if (in == null) {
                throw new IOException("JetBrains Mono font resource not found");
            }

            try {
                return Font.createFont(Font.TRUETYPE_FONT, in)
                        .deriveFont(Font.PLAIN, 16);
            } catch (FontFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public record Holder(Terminal terminal, Closer closer) {

    }
}
