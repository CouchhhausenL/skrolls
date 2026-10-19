package main.java.item.effects;

public class CombatEffect {
    private final CombatEffectType combatEffectType;
    private int duration;
    private int currentRound;
    private double effectStrength;


    public CombatEffect(CombatEffectType combatEffectType, int effectStrength, int duration) {
        this.combatEffectType = combatEffectType;
        this.effectStrength = effectStrength;
        this.duration = duration;
    }

    public double getEffectStrength(){return effectStrength;}
    public int getDuration() {
        return duration;
    }

    public int getCurrentRound(){return currentRound;}
    public boolean tickDuration() {
        duration = duration - 1;
        currentRound++;
        return duration <= 0;
    }
    public CombatEffectType getCombatEffectType(){
        return combatEffectType;
    }
}
