package main.java.battle;

import main.java.DifficultyLevel;
import main.java.MagicType;
import main.java.entity.Entity;
import main.java.entity.Stat;
import main.java.entity.monster.Monster;
import main.java.entity.player.Player;

public class Battle implements BattleContext{
    private Player player;
    private Monster enemy;
    private int round;


    public void fight(){
        player.resetVolatileEffects();
        player.resetStatStages();

    }


    public Entity getPlayer(){
        return player;
    }
    public Entity getEnemy(){
        return enemy;
    }



    public void damageEnemy(int attackBaseDamage, MagicType attackType){
        double baseDamageItemFactor =  player.getBaseDamageItemFactor();
        int baseDamage = AttackHelper.calculateBaseDamage(attackBaseDamage, baseDamageItemFactor);

        int attackStat = player.getAttackStat();
        int attackStatStage = player.getStatStage(Stat.ATTACK);
        double damageItemFactor = player.getDamageItemFactor();
        int attackValue = AttackHelper.calculateAttackValue(attackStat, attackStatStage, damageItemFactor);


        int defenseStat = enemy.getDefenseStat();
        int defenseStatStage = enemy.getStatStage(Stat.DEFENSE);
        int defenseValue = AttackHelper.calculateDefenseValue(defenseStat, defenseStatStage);

        int level = DifficultyLevel.values()[ player.getInventory().getStaff().getQuality().ordinal()].getLevel();
        double stab = 1.0;
        if(player.getInventory().getStaff().getMagicType() == attackType){
            stab = 1.5;
        }
        int critLevel = 1;
        // Here can go checking if potions increase critChance
        double typeEffectiveness = MagicType.getEffectiveness(attackType, enemy.getDefendingMagicType());


        int damage = AttackHelper.calculateDamage(level, baseDamage, attackValue, defenseValue, stab, critLevel, typeEffectiveness);
        enemy.takeDamage(damage);

    }

    public MagicType getEnemyMagicType(){
        return enemy.getDefendingMagicType();
    }






}
