package main.java.item.items;

import main.java.MagicType;
import main.java.item.Item;
import main.java.item.Quality;
import main.java.item.Usable;
import main.java.item.effects.PotionEffect;

import java.util.List;

public class Potion extends Item implements Usable {
    public final Quality quality;
    public final MagicType magicType;
    public final List<PotionEffect> potionEffects;

    public Potion(String name, String description, Quality quality, MagicType magicType, List<PotionEffect> potionEffects) {
        super(name, description);
        this.quality = quality;
        this.magicType = magicType;
        this.potionEffects = potionEffects;

    }

    @Override
    public void use() {

    }
}
