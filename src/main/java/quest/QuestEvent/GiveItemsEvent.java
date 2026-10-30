package main.java.quest.QuestEvent;

import main.java.entity.monster.MonsterType;
import main.java.item.Item;

public record GiveItemsEvent(Item item) implements QuestEvent {}
