package main.java.item;

import main.java.MagicType;

import java.util.function.Supplier;
public enum ItemType {
    MAGIC_STAFF(() -> new Staff("Magic Staff", "Just an staff", MagicType.KINETIC, 10)),
    BASIC_ARMOUR(()-> new Armor("Leather Armor", "Just an armour", MagicType.KINETIC, 2));

    private final Supplier<Item> supplier;

    ItemType(Supplier<Item> supplier) {
        this.supplier = supplier;
    }

    public Item create() {
        return supplier.get();
    }
}