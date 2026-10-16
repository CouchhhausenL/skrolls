package main.java.game;

import main.java.battle.BattleContext;
import main.java.console.Console;
import main.java.console.input.InputEvent;
import main.java.console.input.KeyInputEvent;
import main.java.screen.DrawContext;
import main.java.screen.Screen;

public class Game implements GameContext {
    private final Console console;
    private Screen screen = null;
    private final DrawContext drawContext;

    public Game(Console console) {
        this.console = console;
        drawContext = new DrawContext(console.getWidth(), console.getHeight());
    }

    public void setScreen(Screen screen) {
        screen.setDimensions(console.getWidth(), console.getHeight());
        screen.tryInit(this);
        this.screen = screen;
    }

    public void run() {
        boolean running = true;
        while (running) {
            if (console.areDimensionsDirty()) {
                console.updateDimensions();
                drawContext.resize(console.getWidth(), console.getHeight());
                if (screen != null) {
                    screen.setDimensions(console.getWidth(), console.getHeight());
                    screen.onResize();
                }
            }

            if (screen != null) {
                while (true) {
                    InputEvent event = console.drainEvent();
                    if (event == null) break;

                    if (event instanceof KeyInputEvent key && key.isCtrl(KeyInputEvent.Letter.Q))
                        running = false;

                    screen.onInputEvent(event);
                }
                screen.draw(drawContext);
            }

            console.writeFill(0);
            drawContext.writeToConsole(console);
            console.render();
            drawContext.clear();

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public BattleContext getCurrentBattle() {
        return null;
    }
}
