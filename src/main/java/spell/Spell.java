package main.java.spell;

import main.java.game.GameContext;
import main.java.MagicType;

public abstract class Spell {
    private final String name;
    private final String description;
    private final MagicType magicType;

    public Spell(String name, String description, MagicType magicType) {
        this.name = name;
        this.description = description;
        this.magicType = magicType;

    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void cast(GameContext context);
}
