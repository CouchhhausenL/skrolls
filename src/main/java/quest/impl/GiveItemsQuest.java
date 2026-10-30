package main.java.quest.impl;

import main.java.Interactable.NPC;
import main.java.entity.monster.MonsterType;
import main.java.item.Item;
import main.java.quest.Quest;
import main.java.quest.QuestEvent.GiveItemsEvent;
import main.java.quest.QuestEvent.KillEvent;
import main.java.quest.QuestEvent.QuestEvent;
import main.java.quest.QuestReward;

public class GiveItemsQuest extends Quest {
    private final Item item;
    public GiveItemsQuest(String id,String title, int required, QuestReward reward, NPC npc, Item item){
        super(id, title,required,reward, npc);
        this.item = item;
    }


    @Override
    public boolean handleEvent(QuestEvent event) {
        if(event instanceof GiveItemsEvent(Item givenItem) && givenItem == item){
            return addProgress(1);
        }
        return false;
    }
}
