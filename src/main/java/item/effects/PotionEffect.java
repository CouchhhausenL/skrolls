package main.java.item.effects;

public class PotionEffect {
    private final PotionEffectType potionEffectType;
    private int duration;

    public PotionEffect(PotionEffectType potionEffectType, int duration) {
        this.potionEffectType = potionEffectType;
        this.duration = duration;
    }


    public int getDuration() {
        return duration;
    }

    public boolean tickDuration() {
        duration = duration - 1;
        return duration <= 0;
    }
}
