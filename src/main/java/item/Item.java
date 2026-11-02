package main.java.item;

// Anything the player can carry: has a display name and a description
public interface Item {
    String getName();

    String getDescription();

    ItemType getItemType();

    void setItemType(ItemType itemType);
}
