package main.java.item.effects;

// Categories of item bonuses. Flat types add their value directly;
// multiplier types (below) scale a value (e.g. 1.10 = +10%).
public enum ItemEffectType {

    // General stat boosts, added as a flat value
    MAX_HEALTH,
    MAX_MANA,
    REGENERATION,
    MANA_REGENERATION,
    SPEED,
    DEFENSE,
    ATTACK,
    CRIT_CHANCE,


    // Multipliers (e.g. 1.10 = +10%)
    DAMAGE,       // outgoing damage
    OWN_MAGIC,    // own magic type damage


    // Type-specific resistance multipliers
    RESISTANCE_KINETIC,
    RESISTANCE_ELEMENTAL,
    RESISTANCE_PURITY,
    RESISTANCE_ABYSSAL,
    RESISTANCE_NECROTIC,
    RESISTANCE_DRAGONIC,
    RESISTANCE_ARCANE,
    RESISTANCE_CHAOS,


}
