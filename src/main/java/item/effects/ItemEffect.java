package main.java.item.effects;


public class ItemEffect {
    private final ItemEffectType itemEffectType;
    private final double effectStrength;


    public ItemEffect(ItemEffectType itemEffectType, double effectStrength){
        this.itemEffectType = itemEffectType;
        this.effectStrength = effectStrength;
    }
    public double getEffectStrength() {
        return effectStrength;
    }

    public ItemEffectType getItemEffectType() {
        return itemEffectType;
    }
}
