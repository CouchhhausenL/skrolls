package main.java.item.effects;

public enum PotionEffectType {

    // ---------- Stat Buffs ----------
    HEALING(true),              // instant healing
    MANA_RESTORE(true),         // instant mana restoration
    REGENERATION(true),         // healing over time
    MANA_FLOW(true),            // mana over time
    STRENGTH(true),             // increased attack
    IRON_SKIN(true),            // increased defense
    HASTE(true),                // increased initiative/speed
    SHIELD(true),               // absorbs a fixed amount of damage

    CRITICAL_FOCUS(true),       // higher crit chance
    ACCURACY_UP(true),          // higher hit chance

    FOCUS(true),                // spells cost less mana
    CLEANSE(true),              // removes all debuffs
    INVULNERABILITY(true),      // briefly invulnerable

    // ---------- Resistance Buffs ----------
    KINETIC_WARD(true),
    ELEMENTAL_WARD(true),
    PURITY_WARD(true),
    ABYSSAL_WARD(true),
    NECROTIC_WARD(true),
    DRAGONIC_WARD(true),
    ARCANE_WARD(true),
    CHAOS_WARD(true),

    // ---------- Stat Debuffs ----------
    WEAKNESS(false),            // reduced attack
    FRAGILITY(false),           // reduced defense
    SLOWNESS(false),            // reduced speed
    CURSE(false),               // slightly lowers all stats
    BLINDNESS(false),           // reduced hit chance
    SILENCE(false),             // cannot cast spells
    MANA_BURN(false),           // loses mana over time
    HEALING_REDUCTION(false),   // reduced healing received



;


    private final boolean beneficial;

    PotionEffectType(boolean beneficial) {
        this.beneficial = beneficial;
    }

    public boolean isBeneficial() {
        return beneficial;
    }
}