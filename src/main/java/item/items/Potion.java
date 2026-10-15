package main.java.item.items;

import main.java.MagicType;
import main.java.item.effects.ItemEffect;
import main.java.item.MagicItem;
import main.java.item.Quality;
import main.java.item.Usable;

import java.util.List;

public class Potion extends MagicItem implements Usable {
    public final int duration;

    public Potion(String name, String description, Quality quality, MagicType magicType, List<ItemEffect> itemEffects, int duration){
        super(name,description,quality, magicType, itemEffects);
        this.duration = duration;
    }

    @Override
    public void use() {

    }
}
