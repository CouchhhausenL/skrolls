package main.java.console.factory;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public final class ConsoleTerminalFactory {
    private ConsoleTerminalFactory() {
    }

    public static TerminalHolder create() throws IOException {
        Terminal terminal = TerminalBuilder.terminal();

        CommonTerminalFactory.commonInit(terminal);

        return new TerminalHolder(terminal, () -> CommonTerminalFactory.commonBeforeClose(terminal));
    }
}
