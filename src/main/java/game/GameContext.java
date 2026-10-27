package main.java.game;

import main.java.DifficultyLevel;
import main.java.battle.BattleContext;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;
import main.java.item.Inventory;
import main.java.item.Item;
import main.java.item.items.Potion;
import main.java.scene.Scene;
import main.java.screen.Screen;
import main.java.spell.Spell;
import main.java.verb.VerbSupplier;

import java.util.List;

// Access point for screens and verbs to the running Game's state
public interface GameContext extends VerbSupplier {

    void setScreen(Screen screen);

    BattleContext getCurrentBattle();

    void stop();

    void addMessage(String message);

    List<String> getMessages();

    int getMessagesHashCode();

    void setBattle(Monster monster);

    void fleeFromBattle();

    String getPlayerName();

    void setPlayer(Player player);

    void addItem(Item item);

    boolean equipItem(Item item);

    boolean unequipItem(Item item);

    List<Spell> getSpells();

    List<Potion> getPotions();

    Inventory getInventory();

    void setScene(Scene scene);
    String getSceneName();

    DifficultyLevel getSceneDifficulty();
    DifficultyLevel getPlayerDifficultyLevel();
}
