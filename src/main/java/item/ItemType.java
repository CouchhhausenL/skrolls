package main.java.item;

import main.java.Effect;
import main.java.EffectType;
import main.java.MagicType;

import java.util.List;
import java.util.function.Supplier;
public enum ItemType {
    COMMON_STAFF(() -> new Staff("Old Wooden Cane", "This Staff doesn't really help you, but it looks cool.", Quality.COMMON, MagicType.KINETIC, null)),
    COMMON_ARMOUR(()-> new Armor("Leather Armor", "This may protect you a little bit", Quality.COMMON, MagicType.KINETIC, null)),

    COMMON_HEALTH_AMULETT(()-> new Artifact("Health Amulett", "Increases your maximal Health", Quality.COMMON, MagicType.KINETIC, List.of(new Effect(EffectType.MAX_HEALTH, Quality.COMMON)) )),

    COMMON_HEALTH_POTION(()-> new Potion("Instant Health Potion", "Restores a tiny amount of Health instantly", Quality.COMMON, MagicType.KINETIC, List.of(new Effect(EffectType.HEALING, Quality.COMMON)), 0 )),


    ;




    private final Supplier<Item> supplier;

    ItemType(Supplier<Item> supplier) {
        this.supplier = supplier;
    }

    public Item create() {
        return supplier.get();
    }
}