package main.java.game;

import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.console.Console;
import main.java.console.input.InputEvent;
import main.java.console.input.KeyInputEvent;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;
import main.java.item.Item;
import main.java.item.items.Potion;
import main.java.item.items.Scroll;
import main.java.screen.DrawContext;
import main.java.screen.Screen;
import main.java.screen.impl.MapScreen;
import main.java.spell.Spell;
import main.java.util.Color;
import main.java.verb.Verb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements GameContext {
    public static final int MIN_WIDTH = 80;
    public static final int MIN_HEIGHT = 30;

    private boolean running = false;
    private final Console console;
    private Screen screen = null;

    private Battle battle = null;

    private Player player = null;
    private List<String> messages = new ArrayList<>();

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
    public void setBattle(Monster monster){
        this.battle = new Battle(player, monster);
    }

    @Override
    public List<Potion> getPotions() {
        List<Potion> potions = new ArrayList<>();
        for(Item item: player.getInventory().getBackpackItems()){
            if(item instanceof Potion){
                potions.add((Potion) item);
            }
        }

        return  potions;
    }

    @Override
    public List<Spell> getSpells() {
        return player.getInventory().getScrolls().stream().map(Scroll::getSpell).toList();
    }

    @Override
    public void fleeFromBattle(){
        if(battle.tryFlee()){
            battle.sendMessage("You successfully fled from Battle!");
            setBattle(null);
            setScreen(new MapScreen());
        } else {
            battle.sendMessage("You tried to flee, but it failed");
        }
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String getPlayerName() {
        return player.getName();
    }

    @Override
    public void addItem(Item item) {
        player.getInventory().addItem(item);
    }

    @Override
    public void equipItem(Item item) {
        player.getInventory().equipItem(item);
    }

    @Override
    public BattleContext getCurrentBattle() {
        return battle;
    }


    @Override
    public List<Verb> getVerbs() {
        List<Verb> verbs = new ArrayList<>();
        if (player != null)
            verbs.addAll(player.getInventory().getVerbs());

        return verbs;
    }

    @Override
    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public int getMessagesHashCode() {
        return messages.hashCode();
    }
}
