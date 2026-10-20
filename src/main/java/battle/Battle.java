package main.java.battle;

import main.java.MagicType;
import main.java.entity.Stat;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;

public class Battle implements BattleContext {
    private Player player;
    private Monster enemy;
    private int round;

    public Battle(Player player, Monster monster){
    this.player = player;
    this.enemy = monster;
    }

    public void fight() {
        player.resetCombatEffects();
        player.resetStatStages();
        while (round()) {

        }


    }

    public boolean round() {
        player.applyPeriodicEffects();
        if (player.getSpeed() >= enemy.getSpeed()) {
            if (!playerTurn()) return false;

            if (!monsterAttack()) return false;
        } else {
            if (!monsterAttack()) return false;

            if (!playerTurn()) return false;
        }

        return checkAllAlive();
    }

    public boolean monsterAttack() {
        damagePlayer();
        return checkAllAlive();
    }

    public boolean playerTurn() {
        return checkAllAlive();
    }

    public boolean checkAllAlive() {
        return player.isAlive() && enemy.isAlive();

    }


    public void updatePotionEffects() {

    }


    public String getPlayerName() {
            return player.getName();
    }

    public String getEnemyName() {
        return enemy.getName();
    }
    public int getPlayerHealth(){
        return player.getHealth();
    }
    public int getPlayerMaxHealth(){
        return player.getMaxHealth();
    }

    public int getEnemyHealth(){
        return enemy.getHealth();
    }
    public int getEnemyMaxHealth(){
        return enemy.getMaxHealth();
    }



    public void damagePlayer() {
        int baseDamage = enemy.getDifficulty().getBaseDamage();
        int attackValue = AttackHelper.calculateAttackValue(enemy.getAttackStat(), enemy.getStatStage(Stat.ATTACK), 1.0);

        int defenseValue = AttackHelper.calculateDefenseValue(enemy.getDefenseStat(), enemy.getStatStage(Stat.DEFENSE), 1.0);

        int level = enemy.getDifficulty().getLevel();
        double stab = 1.0;

        int critLevel = 1;

        double typeEffectivness = MagicType.getEffectiveness(enemy.getAttackingMagicType(), player.getDefendingMagicType());

        int damage = AttackHelper.calculateDamage(level, baseDamage, attackValue, defenseValue, stab, critLevel, typeEffectivness);
        player.takeDamage(damage);


    }


    public void damageEnemy(int attackBaseDamage, MagicType attackType) {
        double baseDamageItemFactor = player.getBaseDamageItemFactor();
        int baseDamage = AttackHelper.calculateBaseDamage(attackBaseDamage, baseDamageItemFactor);

        int attackStat = player.getAttackStat();
        int attackStatStage = player.getStatStage(Stat.ATTACK);
        double damageItemFactor = player.getDamageItemFactor();
        int attackValue = AttackHelper.calculateAttackValue(attackStat, attackStatStage, damageItemFactor);


        int defenseStat = player.getDefenseStat();
        int defenseStatStage = player.getStatStage(Stat.DEFENSE);
        double defenseItemFactor = player.getDefenseItemFactor(attackType);
        int defenseValue = AttackHelper.calculateDefenseValue(defenseStat, defenseStatStage, defenseItemFactor);

        int level = player.getLevel();
        double stab = 1.0;
        if (player.getAttackingMagicType() == attackType) {
            stab = 1.5;
        }
        int critLevel = 1;
        // Here can go checking if potions increase critChance


        double typeEffectiveness = MagicType.getEffectiveness(attackType, enemy.getDefendingMagicType());


        int damage = AttackHelper.calculateDamage(level, baseDamage, attackValue, defenseValue, stab, critLevel, typeEffectiveness);
        enemy.takeDamage(damage);

    }

    public MagicType getEnemyMagicType() {
        return enemy.getDefendingMagicType();
    }


}
