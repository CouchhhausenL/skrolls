package main.java.game;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.PlayerLevel;
import main.java.battle.AttackHelper;
import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.console.Console;
import main.java.console.input.InputEvent;
import main.java.console.input.KeyInputEvent;
import main.java.entity.Stat;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;
import main.java.item.Inventory;
import main.java.item.Item;
import main.java.item.Sacrificeable;
import main.java.item.items.Potion;
import main.java.item.items.Scroll;
import main.java.scene.PathScene;
import main.java.scene.Scene;
import main.java.scene.SettlementScene;
import main.java.scene.scenes.WinterfortCastle;
import main.java.screen.DrawContext;
import main.java.screen.Screen;
import main.java.screen.impl.*;
import main.java.spell.Spell;
import main.java.util.Color;
import main.java.util.SplashText;
import main.java.verb.Verb;
import main.java.verb.VerbVariant;

import java.util.*;

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
    private Set<Scene> visitedScenes = new HashSet<>();

    private final Set<String> interactedIds = new HashSet<>();

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
        setScreen(new BattleScreenTransition());
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
    public int getMagicShards() {
        return player.getMagicShards();
    }

    @Override
    public void addMagicShards(int amount) {
        if(amount > 0){
            player.setMagicShards(player.getMagicShards() + amount);
            addMessage("You optained "+ amount + " Magic Shards");
        }
    }

    @Override
    public boolean spendMagicShards(int amount) {
        if(amount > getMagicShards()){
            return false;
        }else{
            player.setMagicShards(getMagicShards() - amount);
            return true;
        }

    }



    @Override
    public int getAttackValue() {
        int attackStat = player.getAttackStat();
        int attackStatStage = player.getStatStage(Stat.ATTACK);
        double damageItemFactor = player.getDamageItemFactor();
        return AttackHelper.calculateAttackValue(attackStat, attackStatStage, damageItemFactor);
    }

    @Override
    public int getDefenseStat() {
        return  AttackHelper.calculateDefenseValue(player.getDefenseStat(),
                player.getStatStage(Stat.DEFENSE), 1.0);
    }

    @Override
    public int getPlayerSpeed() {
        return player.getSpeed();
    }



    @Override
    public DifficultyLevel getPlayerDifficultyLevel() {
        return player.getDifficultyLevel();
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
    public String getSceneName(){
        return scene.getName();
    }

    @Override
    public DifficultyLevel getSceneDifficulty() {
        return scene.getDifficulty();
    }

    @Override
    public List<Spell> getSpells() {
        return player.getInventory().getScrolls().stream().map(Scroll::getSpell).toList();
    }
    public List<Spell> getOutsideSpells(){
        return player.getInventory().getScrolls().stream().map(Scroll::getSpell).filter(Spell::usableOutside).toList();
    }

    // Leaving a battle successfully returns the player to the map
    @Override
    public void fleeFromBattle() {
        if (battle.tryFlee()) {
            MessageScreen messageScreen = new MessageScreen();
            messageScreen.setMessage("You successfully fled from Battle!");
            setScreen(messageScreen);
            messageScreen.setOnComplete(() -> {
                battle = null;
                setScreen(new MainGameScreen());
            });

        } else {
            MessageScreen messageScreen = new MessageScreen();
            messageScreen.setMessage("You tried to flee but it failed");
            setScreen(messageScreen);
            messageScreen.setOnComplete(()->{
                setScreen(new BattleScreen());
            });
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
    public PlayerLevel getPlayerLevel() {
        return player.getPlayerLevel();
    }

    @Override
    public void upgradePlayerLevel() {
        player.upgradePlayerLevel();
    }

    @Override
    public Inventory getInventory(){
        return player.getInventory();
    }

    @Override
    public void addItem(Item item) {
        player.getInventory().addItem(item);
        addMessage("You optained the Item "+item.getName()+" and stashed it in your Inventory!");
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
    public void sacrificeItem(Item item) {
        if (item instanceof Sacrificeable sacrificeableItem) {
            ConfirmationScreen confirmationScreen = new ConfirmationScreen();
            confirmationScreen.setMessage("Do you really want to sell "+item.getName()+" for "+sacrificeableItem.getQuality().getSacrificeValue()+ " Magic Shards");
            setScreen(confirmationScreen);
            confirmationScreen.setOnCancel(() -> setScreen(new InventoryScreen()));

            confirmationScreen.setOnConfirm(()-> {

                int price = player.getInventory().sacrificeItem(sacrificeableItem);
                addMagicShards(price);
                MessageScreen messageScreen = new MessageScreen();
                messageScreen.setMessage("You sacrificed " + sacrificeableItem.getName() + " for " + price + " Magic Shards.\n" +
                        "You now have " + getMagicShards() + " Magic Shards.");
                setScreen(messageScreen);

                messageScreen.setOnComplete(() -> setScreen(new InventoryScreen()));


            });


        } else {
            MessageScreen messageScreen = new MessageScreen();
            messageScreen.setMessage("This item is not sacrificeable!");
            setScreen(messageScreen);
            messageScreen.setOnComplete(() -> setScreen(new InventoryScreen()));
        }

    }


    @Override
    public BattleContext getCurrentBattle() {
        return battle;
    }

    @Override
    public int getPlayerMaxHealth() {
        return player.getMaxHealth();
    }

    @Override
    public int getPlayerMaxMana() {
        return player.getMaxMana();
    }

    @Override
    public Set<Scene> getVisitedScenes() {
        return visitedScenes;
    }
    public void visitScene(Scene targetScene){
        if(visitedScenes.stream().noneMatch(scene -> scene.getName().equals(targetScene.getName()))){
            visitedScenes.add(targetScene);
        }
    }

    @Override
    public boolean hasInteracted(String id) {
        return interactedIds.contains(id);
    }

    @Override
    public void markInteracted(String id) {
        interactedIds.add(id);
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
                                setScreen(new BattleScreenTransition());
                            }else{
                                context.addMessage("This is not a Path scene!");
                                context.addMessage(SplashText.getNo());
                            }
                        }));
        verbs.add(Verb.builder("fastfetch").aliases("neofetch")
                .build((executionContext, context) -> {
                    setScreen(new Fastfetch());
                })
        );

            getOutsideSpells().forEach(spell -> {
                verbs.add(Verb.builder("cast")
                        .aliases("spell")
                        .variant().choice("spell", spell.getName()).build().build()
                        .build((executionContext, context) -> {
                            context.addMessage("Casted spell " + spell.getName());
                            spell.cast(context);
                        }));
            });

        verbs.add(Verb.builder("open").aliases("show", "display")
                .variant()
                .choice("type", "map", "inventory", "ascension")
                .build()
                .build()
                .build(((executionContext, context) -> {
                    String type = executionContext.getString("type");
                    if (type.equals("map"))
                        setScreen(new MapScreen());

                    if (type.equals("inventory"))
                        setScreen(new InventoryScreenOpenTransition());
                    if(type.equals("ascension")){
                        setScreen(new UpgradeScreen());
                    }
                })));


        // One "go <exit>" verb per exit of the current scene
        if (scene != null) {
            scene.getExits().getExits().iterator().forEachRemaining(exit -> {
                String difficulty = exit.resolve(this).getDifficulty().name();
                String name = exit.getDisplayName() + " [" + exit.getId() + "]" + "["+difficulty+"]";
                verbs.add(Verb.builder("go")
                        .aliases("move")
                                .variant().choice("direction", name).build().build()
                        .build((executionContext, context) -> {
                            context.addMessage("Moved to " + name);
                            visitScene(exit.resolve(context));
                            context.setScene(exit.resolve(context));
                        }));
            });

                for (Interactable interactable : scene.getInteractables()){
                        VerbVariant.Builder verbBuilder = Verb.builder(interactable.getVerb()).variant();
                            if(interactable.getPreposition() != null) {
                                verbBuilder.choice("preposition", interactable.getPreposition()).build();
                            }
                            verbBuilder.choice("interactables",interactable.getName()).build();

                            verbs.add(verbBuilder.build()
                            .build((executionContext, context) -> {
                              interactable.onInteract(context);
                            }));

            }
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
