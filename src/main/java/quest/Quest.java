package main.java.quest;

import main.java.Interactable.NPC;
import main.java.quest.QuestEvent.QuestEvent;

import java.util.Objects;

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

    public abstract boolean handleEvent(QuestEvent event);

    protected boolean addProgress(int amount){
        progress = Math.min(required, progress + amount);
        return progress == required;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest quest)) return false;
        return Objects.equals(id, quest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
