package main.java.game;

import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.screen.Screen;

public interface GameContext extends VerbGameContext {

    void setScreen(Screen screen);
    BattleContext getCurrentBattle();
    void stop();

    void setBattle(Battle battle);
}
