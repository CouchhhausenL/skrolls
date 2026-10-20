package main.java.console.factory;

import main.java.console.TerminalType;
import main.java.util.ANSI;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public final class CommonTerminalFactory {
    private CommonTerminalFactory() {
    }

    public static TerminalHolder create(TerminalType type) throws IOException {
        return switch (type) {
            case CONSOLE -> ConsoleTerminalFactory.create();
            case WINDOW -> WindowTerminalFactory.create();
        };
    }

    public static void commonInit(Terminal terminal) {
        // Hide the cursor and draw on the alternate screen so the user's
        // scrollback is left untouched
        terminal.puts(InfoCmp.Capability.cursor_invisible);
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.puts(InfoCmp.Capability.cursor_home);
        terminal.puts(InfoCmp.Capability.enter_ca_mode); // Alternate screen mode

        // Raw mode disables line buffering / echo so every key arrives immediately
        terminal.enterRawMode();
        if (terminal.hasMouseSupport()) {
            terminal.trackMouse(Terminal.MouseTracking.Any);
            terminal.writer().write(ANSI.ENABLE_SGR);
        }

        terminal.flush();
    }

    // Undo every change made by commonInit() before the terminal is closed
    public static void commonBeforeClose(Terminal terminal) {
        terminal.trackMouse(Terminal.MouseTracking.Off);
        terminal.writer().write(ANSI.DISABLE_SGR);

        terminal.puts(InfoCmp.Capability.exit_ca_mode);
        terminal.puts(InfoCmp.Capability.cursor_visible);

        terminal.flush();
    }
}
