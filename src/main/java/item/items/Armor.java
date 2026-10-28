package main.java.item.items;

import main.java.MagicType;
import main.java.item.*;
import main.java.item.effects.ItemEffect;

import java.util.List;

// Defensive equipment that also defines the player's defending magic type
public class Armor extends MagicItem implements Equippable {
    public Armor(String name, String description, Quality quality, MagicType magicType, List<ItemEffect> itemEffects) {
        super(name, description, quality, magicType, itemEffects);
    }

    @Override
    public EquipmentType<Armor> getEquipmentType() {
        return EquipmentType.ARMOR;
    }
}
