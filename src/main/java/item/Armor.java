package main.java.item;

import main.java.MagicType;

public class Armor extends Item implements Equipable {
    private int protection;
    private MagicType protectionType;

    public Armor(String name, String description, MagicType protectionType, int protection) {
        super(name,description);
        this.protection = protection;
        this.protectionType = protectionType;
    }

    @Override
    public void use() {

    }
}
