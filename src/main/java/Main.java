package main.java;

import main.java.console.Console;
import main.java.console.TerminalType;
import main.java.game.Game;
import main.java.screen.impl.StartupScreen;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> argsList = List.of(args);
        // Passing "window" launches the Swing terminal instead of using the host console
        boolean guiFlag = argsList.contains("window");

        Console console = new Console(guiFlag ? TerminalType.WINDOW : TerminalType.CONSOLE);
        Game game = new Game(console);

        // Startup animation leads into the main menu
        game.setScreen(new StartupScreen());
        game.run();

        // Restores the terminal state (raw mode, mouse tracking, alternate screen)
        console.close();
    }
}