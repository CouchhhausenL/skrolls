package main.java.item.effects;

public enum CombatEffectType {

    // ---------- Stat Stage Buffs ---------- -6 to +6

    SPEED(false),                // increased initiative/speed
    STRENGTH(false),             // increased attack
   RESISTANCE(false),  // increased defense

    // -------- Stat Stage Debuffs ------ -6 to +6
    SLOWNESS(false),
    WEAKNESS(false),
    Vulnerability(false),

 // --Periodic Effects ---

 REGENERATION(true),         // healing over time

 MANA_REGEN(true),            // mana over time


    // --- Instant Effects ----
    HEALING(false),              // instant healing
    MANA_RESTORE(false),         // instant mana restoration

    DAMAGE(false), //instant Damage

 DISPEL(false), // dispel all periodic Status Effects


   // Special Effects

    CRITICAL_FOCUS(false),       // higher crit chance 1 - 5
    FOCUS(false),                // spells cost less mana
    CLEANSE(false),              // All Stat Changes go back to 1





 //Status Effects
   BURNED(true), // damage Every Round fixed value
 POISONED(true), // damage Every Round per percent and gets worse
 FROZEN(false), //halves speed
 CONFUSED(true), // chance to hit yourself
 BLEEDING(true), // damage Every Round
 CURSED(true), // max health and max mana go down every round
;
private final boolean periodic;

CombatEffectType(boolean periodic){
 this.periodic = periodic;
}

public Boolean isPeriodic(){
 return periodic;
}

}









