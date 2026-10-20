package main.java.battle;

// Stateless damage math. The formulas are Pokemon-inspired:
// damage depends on level, power, attack/defense ratio, critical hit,
// a random roll, same-type bonus (STAB) and type effectiveness.
public final class AttackHelper {

    // Placeholder for a future dedicated damage attack
    public static void damageAttack() {

    }


    // Applies the item multiplier (e.g. 1.10) to a move's base power
    public static int calculateBaseDamage(int baseDamage, double combinedItemFactor) {
        return (int) Math.floor(baseDamage * combinedItemFactor);
    }

    // Converts a -6..+6 stat stage into a multiplier.
    // Negative stages shrink the stat, positive stages grow it.
    public static double convertStatToDouble(int stat) {
        return switch (stat) {
            case -6 -> 2.0 / 8.0; // 0.25
            case -5 -> 2.0 / 7.0; // ~0.285
            case -4 -> 2.0 / 6.0; // 0.333
            case -3 -> 2.0 / 5.0; // 0.4
            case -2 -> 2.0 / 4.0; // 0.5
            case -1 -> 2.0 / 3.0; // ~0.666
            case 0 -> 1.0; // 1.0
            case 1 -> 3.0 / 2.0; // 1.5
            case 2 -> 4.0 / 2.0; // 2.0
            case 3 -> 5.0 / 2.0; // 2.5
            case 4 -> 6.0 / 2.0; // 3.0
            case 5 -> 7.0 / 2.0; // 3.5
            case 6 -> 8.0 / 2.0; // 4.0
            default -> 1.0; // fallback
        };
    }


    // Final attack value = base attack * stage multiplier * item multiplier
    public static int calculateAttackValue(int attackStat, int attackStatStage, double combinedItemFactor) {
        double sv = convertStatToDouble(attackStatStage);

        return (int) Math.floor(attackStat * sv * combinedItemFactor);

    }

    // Final defense value = base defense * stage multiplier * item multiplier
    public static int calculateDefenseValue(int defenseStat, int defenseStatStage, double combinedItemFactor) {
        double sv = convertStatToDouble(defenseStatStage);
        return (int) Math.floor(defenseStat * sv * combinedItemFactor);
    }


    public static int calculateDamage(int level, int baseDamage, int attack, int defense, double stab, int critLevel, double typeEffectiveness) {
        double crit = 0;
        double requiredCritRandomness = 99;
        // NOTE: no break statements, so these cases fall through and the
        // default assignment below effectively wins for every crit level.
        switch (critLevel) {
            case 0:
                requiredCritRandomness = 99 - 6.25;
            case 1:
                requiredCritRandomness = 99 - 12.5;
            case 2:
                requiredCritRandomness = 99 - 25;
            case 3:
                requiredCritRandomness = 99 - 33.33;
            case 4:
                requiredCritRandomness = 99 - 50;
            default:
                requiredCritRandomness = 99 - 6.25;
        }

        // Roll 1..99; meeting the threshold grants a critical hit
        int critRandomness = (int) (Math.random() * 99) + 1;
        if (critRandomness >= requiredCritRandomness) {
            crit = 0.5;
        }
        // Random damage roll between 85% and 99%
        int zRandomness = (int) (Math.random() * 15);
        int z = 99 - zRandomness;
        // Core formula: level scaling, power, attack/defense, then modifiers
        int damage = (int) Math.floor((((level * 1.0 / 5.0 + 2.0) * baseDamage * attack / (50.0 * defense)) + 2.0) * crit * (z / 100.0) * stab * typeEffectiveness);
        return damage;
    }

}
