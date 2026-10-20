package main.java.console.factory;

import main.java.util.closer.Closer;
import org.jline.terminal.Terminal;

// Bundles the terminal with the cleanup that restores its previous state
public record TerminalHolder(Terminal terminal, Closer closer) {
}
