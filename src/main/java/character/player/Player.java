package main.java.character.player;

import main.java.character.Character;
import main.java.character.Inventory;

public class Player extends Character {
    private int mana;
    private int maxMana;
    private int magicShards;
    private DifficultyLevel difficultyLevel;

    public Player(String name, int maxHealth, int speed, Inventory inventory) {
        super(name, maxHealth, speed, inventory);
    }

    public void cast() {

    }

    public void move() {

    }

    @Override
    public void attack() {

    }
}
