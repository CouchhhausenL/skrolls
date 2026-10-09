package main.java.item;

public abstract class Item {
    private String name;
    private String description;

    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }

    public abstract void use();
}
