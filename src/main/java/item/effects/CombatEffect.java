package main.java.item.effects;

// One active effect on an entity. A duration of 0 marks an instant effect;
// periodic effects decrement duration each round and track currentRound.
public class CombatEffect {
    private final CombatEffectType combatEffectType;
    private int duration;
    private int currentRound;
    private final double effectStrength;


    public CombatEffect(CombatEffectType combatEffectType, double effectStrength, int duration) {
        this.combatEffectType = combatEffectType;
        this.effectStrength = effectStrength;
        this.duration = duration;
    }

    public double getEffectStrength() {
        return effectStrength;
    }

    public int getDuration() {
        return duration;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    // Returns true when the effect has expired and should be removed
    public boolean tickDuration() {
        duration = duration - 1;
        currentRound++;
        return duration <= 0;
    }

    public CombatEffectType getCombatEffectType() {
        return combatEffectType;
    }
}
