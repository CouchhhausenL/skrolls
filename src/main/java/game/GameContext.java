package main.java.game;

import main.java.battle.BattleContext;
import main.java.screen.Screen;

public interface GameContext {

    void setScreen(Screen screen);
    BattleContext getCurrentBattle();

}
