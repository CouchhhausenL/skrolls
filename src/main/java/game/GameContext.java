package main.java.game;

import main.java.DifficultyLevel;
import main.java.PlayerLevel;
import main.java.battle.BattleContext;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;
import main.java.item.Inventory;
import main.java.item.Item;
import main.java.item.items.Potion;
import main.java.quest.Quest;
import main.java.quest.QuestReward;
import main.java.scene.Scene;
import main.java.screen.Screen;
import main.java.spell.Spell;
import main.java.verb.VerbSupplier;

import java.util.List;
import java.util.Set;

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
    PlayerLevel getPlayerLevel();

    void upgradePlayerLevel();

    void setPlayer(Player player);

    void addItem(Item item);

    boolean equipItem(Item item);

    boolean unequipItem(Item item);

    int getMagicShards();

    void sacrificeItem(Item item);
    void addMagicShards(int amount);
    boolean spendMagicShards(int amount);
    int getPlayerSpeed();

    int getAttackValue();
    int getDefenseStat();

    List<Spell> getSpells();

    List<Potion> getPotions();

    Inventory getInventory();

    void setScene(Scene scene);
    String getSceneName();

    DifficultyLevel getSceneDifficulty();
    DifficultyLevel getPlayerDifficultyLevel();

    int getPlayerMaxHealth();
    int getPlayerMaxMana();

    Set<Scene> getVisitedScenes();

    boolean hasInteracted(String id);
    void markInteracted(String id);

    void markUnInteracted(String id);

    Set<Quest> getQuests();
    void addQuest(Quest quest);

    boolean hasQuest(String id);
    Quest getQuest(String id);

    void applyQuestReward(QuestReward reward);
}
