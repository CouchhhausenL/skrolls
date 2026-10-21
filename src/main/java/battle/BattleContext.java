package main.java.battle;

import main.java.MagicType;
import main.java.item.Item;
import main.java.item.effects.CombatEffect;
import main.java.item.items.Potion;

import java.util.List;

// What a spell/effect may do during a battle, kept separate from the
// concrete Battle implementation
public interface BattleContext {

    int damageEnemy(int baseStrength, MagicType magicType);

    int monsterAttack();

    void applyCombatEffectYourself(CombatEffect combatEffect);

    void applyCombatEffectEnemy(CombatEffect combatEffect);


    String getPlayerName();

    String getEnemyName();

    int getPlayerHealth();

    int getPlayerMaxHealth();

    int getEnemyHealth();

    int getEnemyMaxHealth();

    boolean isPlayerAlive();

    boolean isEnemyAlive();

    Item MobDrop();

    boolean tryFlee();

    void drinkPotion(Potion potion);

    void sendMessage(String message);

    List<String> getBattleLog();
    void setBattleState(BattleState battleState);
    BattleState getBattleState();
    Battle.BattleStateOrder determineBattleStateOrder();

    void endRound();
    void fightInit();


}
