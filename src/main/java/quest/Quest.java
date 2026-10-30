package main.java.quest;

import main.java.Interactable.NPC;
import main.java.quest.QuestEvent.QuestEvent;

public abstract class Quest {

    private final String id;
    private final String title;
    private final QuestReward reward;
    private final int required;
    private int progress = 0;
    private final NPC npc;


    protected Quest(String id, String title, int required, QuestReward reward, NPC npc){
        this.id = id;
        this.title = title;
        this.required = required;
        this.reward = reward;
        this.npc = npc;
    }

    public abstract void handleEvent(QuestEvent event);

    protected void addProgress(int amount){
        progress = Math.min(required, progress + amount);
    }
    public boolean isComplete(){
        return progress >= required;
    }
    public QuestReward getReward(){
        return reward;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public int getRequired() {
        return required;
    }

    public NPC getNpc() {
        return npc;
    }

    public String getId() {
        return id;
    }
}
