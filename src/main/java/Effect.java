package main.java;

import main.java.item.Quality;

public class Effect {
    private final EffectType effectType;
    private final Quality effectStrength;


    public Effect (EffectType effectType, Quality effectStrength){
        this.effectType = effectType;
        this.effectStrength = effectStrength;
    }
}
