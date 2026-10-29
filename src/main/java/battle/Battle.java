package main.java.battle;

import main.java.MagicType;
import main.java.entity.Stat;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;
import main.java.item.Item;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.item.items.Potion;

import java.util.ArrayList;
import java.util.List;

// Turn-based battle between the player and one monster.
// Speed decides who acts first each round; the fight ends when either
// combatant is no longer alive.
public class Battle implements BattleContext {
    private final Player player;
    private final Monster enemy;
    private int round;

    private BattleState battleState;

    private final List<String> battleLog = new ArrayList<>();

    public Battle(Player player, Monster monster) {
        this.player = player;
        this.enemy = monster;
    }

    @Override
    public void applyCombatEffectEnemy(CombatEffect combatEffect) {
        enemy.addCombatEffect(combatEffect);
    }

    @Override
    public void applyCombatEffectYourself(CombatEffect combatEffect) {
        player.addCombatEffect(combatEffect);

    }

    @Override
    public boolean tryFlee() {
        // calculate flee chance, gets bigger if player has a higher speed than enemy
        double fleeChance = (double) player.getSpeed() / (player.getSpeed() + enemy.getSpeed());

        return Math.random() < fleeChance;
    }



    // Clears lingering state from previous fights,
    public void fightInit() {
        player.resetCombatEffects();
        enemy.resetCombatEffects();
        player.heal(player.getMaxHealth());
        enemy.heal(enemy.getMaxHealth());
        player.gainMana(player.getMaxMana());





    }

    @Override
    public int getMana() {
        return player.getMana();
    }

    @Override
    public int getMaxMana() {
        return player.getMaxMana();
    }

    @Override
    public boolean spendMana(int amount) {
        return player.spendMana(amount);
    }


    public int monsterAttack() {
        return damagePlayer();
    }
    public void endRound(){
        player.applyPeriodicEffects();
        enemy.applyPeriodicEffects();
        player.tickAllCombatEffects();
        enemy.tickAllCombatEffects();
        player.roundlyRegenerateMana();


        battleState = determineBattleStateOrder().first;
    }

    public void setBattleState(BattleState battleState) {
        this.battleState = battleState;
    }
    public BattleState getBattleState(){
        return battleState;
    }

    public record BattleStateOrder(BattleState first, BattleState second){}

    public BattleStateOrder determineBattleStateOrder(){
        if(player.getSpeed() >= enemy.getSpeed()) {
            battleState = BattleState.PLAYER_TURN;
            return new BattleStateOrder(BattleState.PLAYER_TURN, BattleState.ENEMY_TURN);
        } else {
            battleState = BattleState.ENEMY_TURN;
            return new BattleStateOrder(BattleState.ENEMY_TURN, BattleState.PLAYER_TURN);
        }
    }
     @Override
    public Item MobDrop(){
        Item item = enemy.getMobDrop();
        player.getInventory().addItem(item);
        return item;
    }

    @Override
    public int getMagicShards() {
        return enemy.getDifficulty().getReward();
    }

    public boolean checkAllAlive() {
        return player.isAlive() && enemy.isAlive();

    }

    @Override
    public boolean isPlayerAlive() { return player.isAlive(); }

    @Override
    public boolean isEnemyAlive() { return enemy.isAlive(); }

    @Override
    public void drinkPotion(Potion potion) {
        player.drinkPotion(potion);
    }

    @Override
    public void sendMessage(String message) {
        battleLog.add(message);
    }

    @Override
    public List<String> getBattleLog() {
        return battleLog;
    }


    public String getPlayerName() {
        return player.getName();
    }

    public String getEnemyName() {
        return enemy.getName();
    }

    public int getPlayerHealth() {
        return player.getHealth();
    }

    public int getPlayerMaxHealth() {
        return player.getMaxHealth();
    }

    public int getEnemyHealth() {
        return enemy.getHealth();
    }

    public int getEnemyMaxHealth() {
        return enemy.getMaxHealth();
    }

    public void sendStatusMessage() {
    }


    public int damagePlayer() {
        // Enemy attack uses its own difficulty power and neutral item factors
        int baseDamage = enemy.getDifficulty().getBaseDamage();
        int attackValue = AttackHelper.calculateAttackValue(enemy.getAttackStat(), enemy.getStatStage(Stat.ATTACK), 1.0);

        int defenseValue = AttackHelper.calculateDefenseValue(player.getDefenseStat(),
                player.getStatStage(Stat.DEFENSE),
                player.getDefenseItemFactor(enemy.getAttackingMagicType()));

        int level = enemy.getDifficulty().getLevel();
        double stab = 1.0;

        int critLevel = enemy.getCombinedCombatEffect(CombatEffectType.CRITICAL_FOCUS);

        double typeEffectiveness = MagicType.getEffectiveness(enemy.getAttackingMagicType(), player.getDefendingMagicType());

        int damage = AttackHelper.calculateDamage(level, baseDamage, attackValue, defenseValue, stab, critLevel, typeEffectiveness);
        player.takeDamage(damage);
        sendMessage("You got hit by " + getEnemyName() + "!");
        return damage;


    }



    @Override
    public int damageEnemy(int attackBaseDamage, MagicType attackType) {
        // Player power is scaled by staff/artifact damage effects
        double baseDamageItemFactor = player.getBaseDamageItemFactor();
        int baseDamage = AttackHelper.calculateBaseDamage(attackBaseDamage, baseDamageItemFactor);

        // Offensive side: player attack stat, stage and damage item multiplier
        int attackStat = player.getAttackStat();
        int attackStatStage = player.getStatStage(Stat.ATTACK);
        double damageItemFactor = player.getDamageItemFactor();
        int attackValue = AttackHelper.calculateAttackValue(attackStat, attackStatStage, damageItemFactor);


        // Defensive side:
        // plus resistance matching the spell's magic type
        int defenseStat = enemy.getDefenseStat();
        int defenseStatStage = enemy.getStatStage(Stat.DEFENSE);
        double defenseItemFactor = 1.0;
        int defenseValue = AttackHelper.calculateDefenseValue(defenseStat, defenseStatStage, defenseItemFactor);

        int level = player.getLevel();
        // STAB: 50% bonus when the spell matches the equipped staff's magic type
        double stab = 1.0;
        if (player.getAttackingMagicType() == attackType) {
            stab = 1.5;
        }
        int critLevel = player.getCombinedCombatEffect(CombatEffectType.CRITICAL_FOCUS);


        double typeEffectiveness = MagicType.getEffectiveness(attackType, enemy.getDefendingMagicType());
        if(typeEffectiveness == 0.5){
            sendMessage("You're attack was not very effective!");
        }
        else if (typeEffectiveness == 2.0){
            sendMessage("You're attack was very effective!");
        }



        int damage = AttackHelper.calculateDamage(level, baseDamage, attackValue, defenseValue, stab, critLevel, typeEffectiveness);
        enemy.takeDamage(damage);
        sendMessage("You damaged your enemy!");
        return damage;
    }

    public MagicType getEnemyMagicType() {
        return enemy.getDefendingMagicType();
    }


}
