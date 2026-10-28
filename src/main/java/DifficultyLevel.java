package main.java;

// Maps item quality tiers to combat scaling: level is the effective enemy level,
// baseDamage is the raw damage value used by the damage formula.
// Ordinal order must stay aligned with Quality and the monster difficulty pools.
public enum DifficultyLevel {
    NONE(5, 40, 20),
    LOW(15, 50, 40),
    INTERMEDIATE(25, 60, 80),
    MEDIUM(35, 70, 140),
    HIGH(45, 80, 320),
    EXTREME(65, 90, 600);

    private final int level;
    private final int baseDamage;
    private final int reward;

    DifficultyLevel(int level, int baseDamage, int reward) {
        this.level = level;
        this.baseDamage = baseDamage;
        this.reward = reward;
    }

    public int getLevel() {
        return level;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getReward() {
        return reward;
    }
}
