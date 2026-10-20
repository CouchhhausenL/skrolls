package main.java;

import main.java.console.Console;
import main.java.console.TerminalType;
import main.java.game.Game;
import main.java.screen.impl.MainGameScreen;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> argsList = List.of(args);
        boolean guiFlag = argsList.contains("window");

        Console console = new Console(guiFlag ? TerminalType.WINDOW : TerminalType.CONSOLE);
        Game game = new Game(console);

        game.setScreen(new MainGameScreen());
        game.run();

        console.close();
    }
}