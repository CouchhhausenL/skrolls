package main.java.character;

public abstract class Character {
    private final String name;
    private int health;
    private int maxHealth;
    private int speed;
    private Inventory inventory;

    public Character(String name, int maxHealth, int speed, Inventory inventory){
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.speed = speed;
        this.inventory = inventory;
    }

    public boolean takeDamage(int amount) {
        if (amount < 0) {
            return false;
        }
        health = health - amount;

        return (health > 0);
    }

    public void heal(int amount) {
        if (amount < 0) {
            return;
        }

        health += amount;

        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public int getSpeed(){
        return speed;
    }

    public Inventory getInventory(){
        return inventory;
    }

    public String getName(){
        return name;
    }

    public abstract void attack();
}
