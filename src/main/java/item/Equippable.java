package main.java.item;

// An item that can be moved from the backpack into the equipment store
public interface Equippable extends Item {
    EquipmentType<? extends Equippable> getEquipmentType();
}
