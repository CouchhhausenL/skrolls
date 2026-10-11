package main.java;

import main.java.console.Console;
import main.java.console.TerminalType;
import main.java.util.Color;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> argsList = List.of(args);
        boolean guiFlag = argsList.contains("window");

        Console console = new Console(guiFlag ? TerminalType.WINDOW : TerminalType.CONSOLE);

        while (true) {
            if (console.areDimensionsDirty()) console.updateDimensions();
            console.writeString(2, 1, console.getWidth() + " x " + console.getHeight() + "   ", Color.RED);
            console.render();
            Thread.sleep(1);
        }

        //console.close();
    }
}