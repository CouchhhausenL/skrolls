package main.java.console;

import main.java.console.factory.CommonTerminalFactory;
import main.java.console.factory.TerminalHolder;

import java.io.IOException;

// Which kind of terminal to create: the host console or a Swing window
public enum TerminalType {
    CONSOLE,
    WINDOW;

    public TerminalHolder create() throws IOException {
        return CommonTerminalFactory.create(this);
    }
}
