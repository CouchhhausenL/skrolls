package main.java.battle;

import main.java.MagicType;
import main.java.item.effects.CombatEffect;

import java.util.List;

public interface BattleContext {

    void damageEnemy(int baseStrength, MagicType magicType);

    void applyCombatEffectYourself(CombatEffect combatEffect);
    void applyCombatEffectEnemy(CombatEffect combatEffect);



    String getPlayerName();
    String getEnemyName();

    int getPlayerHealth();
    int getPlayerMaxHealth();
    int getEnemyHealth();
    int getEnemyMaxHealth();

    boolean tryFlee();

    void sendMessage(String message);
    List<String> getBattleLog();

}
