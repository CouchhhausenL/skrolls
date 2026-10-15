package main.java.item.effects;

public class PotionEffect {
    private final PotionEffectType potionEffectType;
    private final int duration;

    public PotionEffect(PotionEffectType potionEffectType, int duration){
        this.potionEffectType = potionEffectType;
        this.duration = duration;
    }
}
