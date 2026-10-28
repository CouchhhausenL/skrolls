package main.java;

// Maps item quality tiers to combat scaling: level is the effective enemy level,
// baseDamage is the raw damage value used by the damage formula.
// Ordinal order must stay aligned with Quality and the monster difficulty pools.
public enum PlayerLevel {
    NOVICE(5, 0, 60, 60, 40,50, 100, 5),
    APPRENTICE(15, 200,80,70,60,70, 150, 10),
    ADEPT(25, 500, 100, 85, 75, 80, 200, 15),
    SORCERER(35, 1000, 120, 100, 90,90, 300, 20),
    ARCHMAGE(45, 2000, 140, 110, 110, 105, 400, 30),
    GRAND_ARCH_SOURCERER (65, 5000, 180, 130, 130, 120, 600, 40);

    private final int level;
    private final int baseDefense;
    private final int baseAttack;
    private final int baseSpeed;
    private final int baseMaxHealth;

    private final int baseMaxMana;
    private final int manaRegen;
    private final int cost;

    PlayerLevel(int level, int cost, int baseMaxHealth, int  baseSpeed, int baseAttack, int baseDefense, int baseMaxMana, int manaRegen) {
        this.level = level;
        this.cost = cost;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpeed = baseSpeed;
        this.baseMaxHealth = baseMaxHealth;
        this.baseMaxMana = baseMaxMana;
        this.manaRegen = manaRegen;
    }

    public int getLevel() {
        return level;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseMaxHealth() {
        return baseMaxHealth;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getBaseMaxMana() {
        return baseMaxMana;
    }

    public int getManaRegen() {
        return manaRegen;
    }

    public int getCost() {
        return cost;
    }
}
