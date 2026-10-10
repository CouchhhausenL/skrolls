package main.java.item;

import main.java.MagicType;

public class Potion extends Item {
    private MagicType potionType;

    public Potion(String name, String description) {
        super(name, description);
    }

    @Override
    public void use() {

    }
}
