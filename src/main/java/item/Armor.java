package main.java.item;

import main.java.MagicType;

public class Armor extends Item implements Equipable {
    private final int protection;
    private final MagicType protectionType;

    public Armor(String name, String description, MagicType protectionType, int protection) {
        super(name,description);
        this.protection = protection;
        this.protectionType = protectionType;
    }

    public int getProtection() {
        return protection;
    }

    public MagicType getProtectionType() {
        return protectionType;
    }
}
