package main.java.item;

// Base implementation holding the display name and description
public abstract class AbstractItem implements Item {
    private final String name;
    private final String description;

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
}
