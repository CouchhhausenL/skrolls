package main.java.item;

public interface Equipable extends Item {
    EquipmentType<? extends Equipable> getEquipmentType();
}
