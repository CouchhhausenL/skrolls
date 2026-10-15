package main.java.item;

import main.java.MagicType;
import main.java.item.effects.ItemEffect;
import main.java.item.effects.ItemEffectType;
import main.java.item.effects.PotionEffect;
import main.java.item.effects.PotionEffectType;
import main.java.item.items.Armor;
import main.java.item.items.Artifact;
import main.java.item.items.Potion;
import main.java.item.items.Staff;

import java.util.List;
import java.util.function.Supplier;

public enum ItemType {
    COMMON_STAFF(() -> new Staff("Old Wooden Cane", "This Staff doesn't really help you, but it looks cool.", Quality.COMMON, MagicType.KINETIC, null)),
    COMMON_ARMOUR(()-> new Armor("Leather Armor", "This may protect you a little bit", Quality.COMMON, MagicType.KINETIC, null)),

    COMMON_HEALTH_AMULET(()-> new Artifact("Health Amulet", "Increases your maximal Health", Quality.COMMON, MagicType.KINETIC, List.of(new ItemEffect(ItemEffectType.MAX_HEALTH, Quality.COMMON)))),

    COMMON_HEALTH_POTION(()-> new Potion("Instant Health Potion", "Restores a tiny amount of Health instantly", Quality.COMMON, MagicType.KINETIC, List.of(new PotionEffect(PotionEffectType., 10)), 0));

    private final Supplier<Item> supplier;

    ItemType(Supplier<Item> supplier) {
        this.supplier = supplier;
    }

    public Item create() {
        return supplier.get();
    }
}