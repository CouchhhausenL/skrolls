package main.java.quest.impl;

import main.java.Interactable.NPC;
import main.java.entity.monster.MonsterType;
import main.java.quest.Quest;
import main.java.quest.QuestEvent.KillEvent;
import main.java.quest.QuestEvent.QuestEvent;
import main.java.quest.QuestReward;

public class KillQuest extends Quest {
    private final MonsterType monsterType;
    public KillQuest(String id, String title, int required, QuestReward reward, NPC npc, MonsterType monsterType){
        super(id, title,required,reward, npc);
        this.monsterType = monsterType;
    }


    @Override
    public boolean handleEvent(QuestEvent event) {
        if(event instanceof KillEvent(MonsterType type) && type == monsterType){
            return addProgress(1);
        }
        return false;
    }
}
