package main.java.battle;

import main.java.MagicType;

public interface BattleContext {

    void damageEnemy(int baseStrength, MagicType magicType);

    String getPlayerName();
    String getEnemyName();

    int getPlayerHealth();
    int getPlayerMaxHealth();
    int getEnemyHealth();
    int getEnemyMaxHealth();

}
