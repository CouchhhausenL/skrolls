package main.java;

import main.java.console.Console;
import main.java.console.TerminalType;
import main.java.screen.impl.TestScreen;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> argsList = List.of(args);
        boolean guiFlag = argsList.contains("window");

        Console console = new Console(guiFlag ? TerminalType.WINDOW : TerminalType.CONSOLE);
        Game game = new Game(console);

        game.setScreen(new TestScreen());
        game.run();

        console.close();
    }
}