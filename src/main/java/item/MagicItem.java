package main.java.item;

import main.java.MagicType;
import main.java.item.effects.ItemEffect;
import main.java.item.effects.ItemEffectType;

import java.util.List;

public abstract class MagicItem extends Item {
    private final Quality quality;
    private final MagicType magicType;
    private final List<ItemEffect> itemEffects;

    public MagicItem(String name, String description, Quality quality, MagicType magicType, List<ItemEffect> itemEffects) {
        super(name, description);
        this.magicType = magicType;
        this.quality = quality;
        this.itemEffects = itemEffects;
    }


    public MagicType getMagicType() {
        return magicType;
    }

    public List<ItemEffect> getItemEffects() {
        return itemEffects;
    }

    public double getItemEffectStrength(ItemEffectType effectType) {
        double itemEffectStrength = 0;
        for (ItemEffect currentEffect : itemEffects) {
            if (currentEffect.itemEffectType() == effectType) {
                itemEffectStrength = itemEffectStrength + currentEffect.effectStrength();
            }
        }
        return itemEffectStrength;
    }


    public Quality getQuality() {
        return quality;
    }
}
