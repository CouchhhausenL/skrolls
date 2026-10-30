package main.java.screen.widget.impl.options;

import main.java.item.Item;
import main.java.quest.Quest;

public class QuestOption extends Option {
    private Quest quest;

    public QuestOption(String name, int color, Quest quest, Runnable runnable) {
        super(name, color, runnable);
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }
}

