package main.java.item;

import main.java.MagicType;

public class Staff extends Item implements Equipable {
    private final MagicType damageType;
    private final int power;

    public Staff(String name, String description, MagicType damageType, int power) {
        super(name,description);
        this.damageType = damageType;
       this.power = power;
    }

    public MagicType getDamageType() {
        return damageType;
    }

    public int getPower() {
        return power;
    }

    @Override
    public EquipmentType getEquipmentType() {
        return EquipmentType.STAFF;
    }
}
