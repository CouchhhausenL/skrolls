package main.java.item.items;

import main.java.MagicType;
import main.java.item.MagicItem;
import main.java.item.Quality;
import main.java.item.Usable;
import main.java.item.effects.ItemEffect;

import java.util.List;

public class Staff extends MagicItem implements Usable {

    public Staff(String name, String description, Quality quality, MagicType magicType, List<ItemEffect> itemEffects) {
        super(name, description, quality, magicType, itemEffects);
    }

    @Override
    public void use() {

    }

}
