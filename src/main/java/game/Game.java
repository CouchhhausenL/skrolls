package main.java.game;

import main.java.battle.BattleContext;
import main.java.console.Console;
import main.java.console.input.InputEvent;
import main.java.console.input.KeyInputEvent;
import main.java.screen.DrawContext;
import main.java.screen.Screen;
import main.java.util.Color;
import main.java.verb.Verb;

import java.util.List;

public class Game implements GameContext {
    public static final int MIN_WIDTH = 80;
    public static final int MIN_HEIGHT = 30;

    private boolean running = false;
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
        running = true;
        while (running) {
            if (console.areDimensionsDirty()) {
                console.updateDimensions();
                drawContext.resize(console.getWidth(), console.getHeight());
                if (screen != null) {
                    screen.setDimensions(console.getWidth(), console.getHeight());
                    screen.onResize();
                }
            }

            final boolean isWidthTooLow = console.getWidth() < MIN_WIDTH;
            final boolean isHeightTooLow = console.getHeight() < MIN_HEIGHT;
            final boolean isTooSmall = isWidthTooLow || isHeightTooLow;

            if (screen != null && !isTooSmall) {
                while (true) {
                    InputEvent event = console.drainEvent();
                    if (event == null) break;

                    if (event instanceof KeyInputEvent key && key.isCtrl(KeyInputEvent.Letter.Q))
                        stop();

                    screen.onInputEvent(event);
                }
                screen.draw(drawContext);
            }

            if (isTooSmall) {
                drawContext.drawString(1, 1, "Terminal is too small!", Color.WHITE);
                drawContext.drawString(1, 3, "x: " + console.getWidth() + " / " + MIN_WIDTH, isWidthTooLow ? Color.RED : Color.GREEN);
                drawContext.drawString(1, 4, "y: " + console.getHeight() + " / " + MIN_HEIGHT, isHeightTooLow ? Color.RED : Color.GREEN);
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

    public void stop() {
        running = false;
    }

    @Override
    public BattleContext getCurrentBattle() {
        return null;
    }


    @Override
    public List<Verb> getVerbs() {
        return List.of(
                Verb.builder("use")
                        .aliases("drink", "consume", "chug")
                        .variant()
                            .choice("item", "water", "healing potion", "resistance potion", "healing potion deluxe", "healing health boost potion").build()
                            .build()
                        .variant()
                            .choice("item", "water", "healing potion", "resistance potion", "healing potion deluxe", "healing health boost potion").build()
                            .integer("count", 1, 10).build()
                            .choice("times", "times").build()
                            .build()
                        .variant()
                            .integer("count", 1, 10).build()
                            .choice("item", "healing potions", "resistance potions").build()
                            .build()
                        .build((executionContext, context) -> {
                        }),

                Verb.builder("use")
                        .aliases("eat", "consume")
                        .variant()
                            .choice("item", "pizza", "burger").build()
                            .build()
                        .build(((executionContext, context) -> {

                        })),

                Verb.builder("leave")
                        .build(((executionContext, context) -> {
                            context.stop();
                        }))
        );
    }
}
