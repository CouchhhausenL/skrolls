package main.java.console;

import main.java.console.factory.CommonTerminalFactory;
import main.java.console.factory.TerminalHolder;

import java.io.IOException;

public enum TerminalType {
    CONSOLE,
    WINDOW;

    public TerminalHolder create() throws IOException {
        return CommonTerminalFactory.create(this);
    }
}
