package main.java.game;

import main.java.battle.BattleContext;
import main.java.screen.Screen;
import main.java.verb.Verb;

import java.util.List;

public interface GameContext {

    void setScreen(Screen screen);
    BattleContext getCurrentBattle();
    void stop();
    List<Verb> getVerbs();

}
