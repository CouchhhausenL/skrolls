package main.java.item;

// Item rarity tiers. The order is significant: ordinals are used to index
// DifficultyLevel and to rank the best item in an inventory.
public enum Quality {
    COMMON(10),
    UNCOMMON(30),
    RARE(50),
    EPIC(100),
    LEGENDARY(200),
    MYTHIC(500)
    ;
    private final int sacrificeValue;

    Quality(int sacrificeValue){
        this.sacrificeValue = sacrificeValue;
    }
    public int getSacrificeValue(){
        return sacrificeValue;
    }
}
