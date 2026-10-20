package main.java.game;

import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.entity.player.Player;
import main.java.screen.Screen;
import main.java.verb.Verb;

import java.util.List;

public interface GameContext {

    void setScreen(Screen screen);
    BattleContext getCurrentBattle();
    void stop();
    List<Verb> getVerbs();

    void addMessage(String message);
    List<String> getMessages();
    int getMessagesHashCode();

    void setBattle(Battle battle);

    void fleeFromBattle();

    Player getPlayer();
    void setPlayer(Player player);
}
