package main.java.game;

import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;
import main.java.item.Item;
import main.java.item.items.Potion;
import main.java.screen.Screen;
import main.java.spell.Spell;
import main.java.verb.Verb;

import java.util.List;

public interface GameContext {

    void setScreen(Screen screen);
    BattleContext getCurrentBattle();
    void stop();
    List<Verb> getVerbs();


    void setBattle(Monster monster);

    void fleeFromBattle();

    String getPlayerName();
    void setPlayer(Player player);
    void addItem(Item item);
    void equipItem(Item item);

    List<Spell> getSpells();
    List<Potion> getPotions();
}
