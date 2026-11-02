package main.java.item;

// Base implementation holding the display name and description
public abstract class AbstractItem implements Item {
    private final String name;
    private final String description;
    private ItemType itemType; // set by ItemType.create() after construction

    public AbstractItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public ItemType getItemType() {
        return itemType;
    }

    // Called by ItemType.create() to inject the enum constant after construction
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
