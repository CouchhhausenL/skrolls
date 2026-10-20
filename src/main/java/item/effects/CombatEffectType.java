package main.java.item.effects;

// All combat effects and whether they recur every round (periodic)
// or fire once when applied (instant / special).
public enum CombatEffectType {

    // ---------- Stat Stage Buffs: shift a stage by -6..+6 ----------
    SPEED(false),                // more initiative
    STRENGTH(false),             // more attack
    RESISTANCE(false),           // more defense

    // ---------- Stat Stage Debuffs: negative stage shifts ----------
    SLOWNESS(false),             // less initiative
    WEAKNESS(false),             // less attack
    Vulnerability(false),        // less defense

    // ---------- Periodic Effects: applied at the start of each round ----------
    REGENERATION(true),          // heals a fixed percentage of max health

    MANA_REGEN(true),            // restores mana

    // ---------- Instant Effects: applied once, then removed ----------
    HEALING(false),              // flat healing
    MANA_RESTORE(false),         // flat mana restoration

    DAMAGE(false),               // flat damage

    DISPEL(false),               // removes all periodic effects

    // ---------- Special Effects ----------
    CRITICAL_FOCUS(false),       // raises crit chance, level 1..5
    FOCUS(false),                // reduces spell mana cost
    CLEANSE(false),              // resets all stat stages to 0

    // ---------- Status Effects ----------
    BURNED(true),                // fixed percentage damage every round, weakens over time
    POISONED(true),              // percentage damage every round, worsens over time
    FROZEN(false),               // halves speed
    CONFUSED(true),              // random chance to damage itself
    BLEEDING(true),              // percentage damage every round
    CURSED(true),                // drains max health and max mana every round
    ;
    private final boolean periodic;

    CombatEffectType(boolean periodic) {
        this.periodic = periodic;
    }

    public Boolean isPeriodic() {
        return periodic;
    }

}









