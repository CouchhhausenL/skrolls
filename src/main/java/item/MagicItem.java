package main.java.item;

import main.java.Effect;
import main.java.MagicType;

import java.util.List;

public abstract class MagicItem extends Item{
    private Quality quality;
    private MagicType magicType;
    private List<Effect> effects;
    private Effect typeExclusiveEffect;

    public MagicItem (String name, String description, Quality quality,  MagicType magicType , List<Effect> effects){
        super(name, description);
        this.magicType = magicType;
        this.quality = quality;
        this.effects = effects;
        this.typeExclusiveEffect = new Effect(magicType.getExclusiveEffectType(), quality);
    }

}
