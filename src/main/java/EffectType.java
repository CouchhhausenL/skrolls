package main.java;

public enum EffectType {

    //general stat boosts
    MAX_HEALTH,
    HEALING,
    MANA_REGEN,
    MANA_GAIN,
    MANA_DRAIN,
    SPEED,
    SLOWNESS,
    DEFENSE,
    DAMAGE,

    // specific resistances against types
    RESISTANCE_KINETIC,
    RESISTANCE_ELEMENTAL,
    RESISTANCE_PURITY,
    RESISTANCE_ABYSSAL,
    RESISTANCE_NECROTIC,
    RESISTANCE_DRAGONIC,
    RESISTANCE_ARCANE,
    RESISTANCE_CHAOS,



    // magic exclusives
    BURN, // chance to burn opponent
    BLESSING, // heals yourself every round
    CURSE, // chance to curse opponent
    LIFE_STEAL, // heals yourself a percent of your damage
    ROUGH, // resists all effects like curse or burn
    ARCANE_FOCUS, // Reduces Mana costs, increases damage boost
    PURE_CHAOS, // deals fluctuating damage sometimes critical
    INFINITE // No mana Costs

}
