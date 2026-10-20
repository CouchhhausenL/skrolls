package main.java.battle;

import main.java.MagicType;
import main.java.item.effects.CombatEffect;
import main.java.item.items.Potion;

import java.util.List;

public interface BattleContext {

    int damageEnemy(int baseStrength, MagicType magicType);

    void applyCombatEffectYourself(CombatEffect combatEffect);
    void applyCombatEffectEnemy(CombatEffect combatEffect);



    String getPlayerName();
    String getEnemyName();

    int getPlayerHealth();
    int getPlayerMaxHealth();
    int getEnemyHealth();
    int getEnemyMaxHealth();

    boolean tryFlee();

    void drinkPotion(Potion potion);

    void sendMessage(String message);
    List<String> getBattleLog();

}
