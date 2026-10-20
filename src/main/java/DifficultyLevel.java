package main.java;

// Maps item quality tiers to combat scaling: level is the effective enemy level,
// baseDamage is the raw damage value used by the damage formula.
// Ordinal order must stay aligned with Quality and the monster difficulty pools.
public enum DifficultyLevel {
    NONE(5, 40),
    LOW(15, 50),
    INTERMEDIATE(25, 60),
    MEDIUM(35, 70),
    HIGH(45, 80),
    EXTREME(65, 90);

    private final int level;
    private final int baseDamage;

    DifficultyLevel(int level, int baseDamage) {
        this.level = level;
        this.baseDamage = baseDamage;
    }

    public int getLevel() {
        return level;
    }

    public int getBaseDamage() {
        return baseDamage;
    }


}
