package main.java.console.factory;

import main.java.util.closer.Closer;
import org.jline.terminal.Terminal;

public record TerminalHolder(Terminal terminal, Closer closer) {
}
