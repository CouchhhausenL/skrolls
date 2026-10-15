package main.java.entity.monster;

import main.java.MagicType;
import main.java.entity.Entity;

public class Monster extends Entity {
    private final String description;
    private final MagicType magicType;
    private final int attack;
    private final int defense;

    public Monster(String name, String description, int maxHealth, int speed, int attack, int defense, MagicType magicType){
        super(name, maxHealth, speed);
        this.description = description;
        this.magicType = magicType;
        this.attack = attack;
        this.defense = defense;
    }

    public void attack() {

    }

    @Override
    public int getDefenseStat(){
        return defense;
    }

    @Override
    public MagicType getDefendingMagicType() {
        return magicType;
    }
}
