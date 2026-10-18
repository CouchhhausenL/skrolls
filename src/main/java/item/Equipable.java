package main.java.item;
public interface Equipable {
    EquipmentType<? extends Equipable> getEquipmentType();
}
