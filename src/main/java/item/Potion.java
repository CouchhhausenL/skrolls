package main.java.item;

import main.java.MagicType;

public class Potion extends Item {
    private final MagicType potionType;

    public Potion(String name, String description, MagicType potionType) {
        super(name, description);
        this.potionType = potionType;
    }

    @Override
    public void use() {

    }

    public MagicType getPotionType() {
        return potionType;
    }
}
