package main.java.item;

public interface Equippable extends Item {
    EquipmentType<? extends Equippable> getEquipmentType();
}
