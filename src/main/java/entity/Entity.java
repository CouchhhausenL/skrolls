package main.java.entity;

import main.java.MagicType;
import main.java.item.effects.PotionEffect;

import java.util.*;


public abstract class Entity {
    private final String name;
    private int health;
    private final int maxHealth;
    private final int speed;
    private final Set<VolatileEffect> volatileEffects = EnumSet.noneOf(VolatileEffect.class);
    private final Map<Stat, Integer> statStages = new EnumMap<>(Stat.class);
    private final List<PotionEffect> potionEffects = new ArrayList<>();


    public Entity(String name, int maxHealth, int speed) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.speed = speed;

    }

    public boolean takeDamage(int amount) {
        if (amount < 0) {
            return false;
        }
        health = health - amount;

        return (health > 0);
    }

    public void heal(int amount) {
        if (amount < 0) {
            return;
        }

        health += amount;

        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public int getStatStage(Stat stat) {
        return statStages.getOrDefault(stat, 0);
    }


    public void modifyStatStage(Stat stat, int amount) {
        int current = getStatStage(stat);
        int updated = Math.clamp(current + amount, -6, 6);
        statStages.put(stat, updated);
    }

    public void setStatStage(Stat stat, int stage) {
        int updated = Math.clamp(stage, -6, 6);
        statStages.put(stat, updated);
    }

    public void resetVolatileEffects() {
        volatileEffects.clear();
    }


    public void resetStatStages() {
        statStages.replaceAll((s, v) -> 0);
    }


    public void resetStatStage(Stat stat) {
        statStages.put(stat, 0);
    }

    public Set<VolatileEffect> getVolatileEffects() {
        return volatileEffects;
    }

    public boolean hasVolatileEffect(VolatileEffect effect) {
        return volatileEffects.contains(effect);
    }

    public void addVolatileEffect(VolatileEffect effect) {
        volatileEffects.add(effect);
    }

    public void removeVolatileEffect(VolatileEffect effect) {
        volatileEffects.remove(effect);
    }


    public void addPotionEffect(PotionEffect potionEffect) {
        potionEffects.add(potionEffect);
    }

    public void removePotionEffect(PotionEffect potionEffect) {
        potionEffects.remove(potionEffect);
    }

    public void tickAllPotionEffects() {
        potionEffects.removeIf(PotionEffect::tickDuration);
    }


    public Boolean isAlive() {
        return health > 0;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public abstract int getDefenseStat();

    public abstract MagicType getDefendingMagicType();

    public abstract MagicType getAttackingMagicType();

    public abstract int getLevel();

    public abstract int getAttackStat();


    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
    return maxHealth;
    }
}
