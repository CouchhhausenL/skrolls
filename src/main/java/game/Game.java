package main.java.game;

import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.console.Console;
import main.java.console.input.InputEvent;
import main.java.console.input.KeyInputEvent;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;
import main.java.item.Inventory;
import main.java.item.Item;
import main.java.item.items.Potion;
import main.java.item.items.Scroll;
import main.java.scene.PathScene;
import main.java.scene.Scene;
import main.java.scene.scenes.Winterfort;
import main.java.scene.scenes.WinterfortCastle;
import main.java.screen.DrawContext;
import main.java.screen.Screen;
import main.java.screen.impl.BattleScreen;
import main.java.screen.impl.InventoryScreen;
import main.java.screen.impl.MainGameScreen;
import main.java.screen.impl.MapScreen;
import main.java.spell.Spell;
import main.java.util.Color;
import main.java.util.SplashText;
import main.java.verb.Verb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Central game loop and state holder. Owns the active screen, battle,
// player, scene and the message log, and pumps input/render each tick.
public class Game implements GameContext {
    public static final int MIN_WIDTH = 80;
    public static final int MIN_HEIGHT = 30;

    private boolean running = false;
    private final Console console;
    private Screen screen = null;

    private Battle battle = null;
    private Player player = null;
    private Scene scene = new WinterfortCastle(this);


    private final List<String> messages = new ArrayList<>();

    private final DrawContext drawContext;

    public Game(Console console) {
        this.console = console;
        drawContext = new DrawContext(console.getWidth(), console.getHeight());
    }

    // Switching screens re-initializes only if the screen was not initialized yet
    public void setScreen(Screen screen) {
        screen.setDimensions(console.getWidth(), console.getHeight());
        screen.tryInit(this);
        this.screen = screen;
    }

    // Main loop: handle resize, drain input, draw, then present to the console
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

            // Drain all pending input before drawing so the frame reflects it
            if (screen != null && !isTooSmall) {
                while (true) {
                    InputEvent event = console.drainEvent();
                    if (event == null) break;

                    // Ctrl+Q is a global quit shortcut
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

            // Clear, blit the draw context, present and reset for the next frame
            console.writeFill(0);
            drawContext.writeToConsole(console);
            console.render();
            drawContext.clear();

            // Small sleep caps the loop at roughly 200 FPS
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
    public void setBattle(Monster monster) {
        this.battle = new Battle(player, monster);
    }

    @Override
    public List<Potion> getPotions() {
        List<Potion> potions = new ArrayList<>();
        for (Item item : player.getInventory().getBackpackItems()) {
            if (item instanceof Potion) {
                potions.add((Potion) item);
            }
        }

        return potions;
    }

    @Override
    public void setScene(Scene scene) {
        if(player.getDifficultyLevel().ordinal() >= scene.getDifficulty().ordinal()){
            this.scene = scene;
        }
        else {

            addMessage(SplashText.getNo());
            addMessage("This Scene is to difficult for you! " +"[" + scene.getDifficulty() + "]");
        }
    }

    @Override
    public List<Spell> getSpells() {
        return player.getInventory().getScrolls().stream().map(Scroll::getSpell).toList();
    }

    // Leaving a battle successfully returns the player to the map
    @Override
    public void fleeFromBattle() {
        if (battle.tryFlee()) {
            battle.sendMessage("You successfully fled from Battle!");
            setBattle(null);
            setScreen(new MainGameScreen());
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
    public Inventory getInventory(){
        return player.getInventory();
    }

    @Override
    public void addItem(Item item) {
        player.getInventory().addItem(item);
    }

    @Override
    public boolean equipItem(Item item) {
        return player.getInventory().equipItem(item);
    }

    @Override
    public boolean unequipItem(Item item) {
        return player.getInventory().unequipItem(item);
    }

    @Override
    public BattleContext getCurrentBattle() {
        return battle;
    }


    // Rebuilt every keystroke by the verb input widget: global verbs,
    // scene exits (when a scene is active) and inventory equip/unequip verbs
    @Override
    public List<Verb> getVerbs() {
        List<Verb> verbs = new ArrayList<>();

        verbs.add(Verb.builder("quit")
                .aliases("leave", "stop", "end")
                .variant()
                .choice("game", "game")
                .build()
                .build()
                .build((executionContext, context) -> {
                    context.stop();
                }));

        verbs.add(Verb.builder("fight").aliases("battle", "engage")
                        .build((executionContext, context) -> {
                            if(scene instanceof PathScene){
                                setBattle(((PathScene) scene).spawnMonster());
                                setScreen(new BattleScreen());
                            }else{
                                context.addMessage(SplashText.getNo());
                            }
                        }));

        verbs.add(Verb.builder("open").aliases("show", "display")
                .variant()
                .choice("type", "map", "inventory")
                .build()
                .build()
                .build(((executionContext, context) -> {
                    String type = executionContext.getString("type");
                    if (type.equals("map"))
                        setScreen(new MapScreen());

                    if (type.equals("inventory"))
                        setScreen(new InventoryScreen());
                })));

        // One "go <exit>" verb per exit of the current scene
        if (scene != null) {
            scene.getExits().getExits().iterator().forEachRemaining(exit -> {
                String name = exit.getDisplayName() + " [" + exit.getId() + "]" + "[";
                verbs.add(Verb.builder("go")
                        .aliases("move")
                                .variant().choice("direction", name).build().build()
                        .build((executionContext, context) -> {
                            context.addMessage("Moved to " + name);
                            context.setScene(exit.resolve(context));
                        }));
            });
        }

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
