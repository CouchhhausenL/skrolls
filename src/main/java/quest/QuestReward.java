package main.java.quest;

import main.java.item.Item;

import java.util.List;

public record QuestReward(int magicShards, List<Item> items) {
    public QuestReward(int magicShards){
        this(magicShards, List.of());
    }
    public QuestReward(int magicShards, Item item){
        this(magicShards, List.of(item));
    }
    public QuestReward(Item item){
        this(0,item);
    }
}
