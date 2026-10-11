package main.java.character.monster;

import main.java.MagicType;
import main.java.character.Character;
import main.java.character.Inventory;

public class Monster extends Character {
    private final String description;
    private final MagicType magicType;

    public Monster(String name, String description, int maxHealth, int speed, Inventory inventory, MagicType magicType){
        super(name, maxHealth, speed, inventory);
        this.description = description;
        this.magicType = magicType;
    }

    public void attack() {

    }
}
