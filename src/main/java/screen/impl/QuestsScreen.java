package main.java.screen.impl;

import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.item.Item;
import main.java.quest.Quest;
import main.java.screen.Screen;
import main.java.screen.widget.impl.BattleStatusWidget;
import main.java.screen.widget.impl.ChatMessageWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.screen.widget.impl.options.Option;
import main.java.screen.widget.impl.options.QuestOption;
import main.java.util.Color;

public class QuestsScreen extends Screen {
    private OptionsWidget options;
    private ScrollableContainerWidget details;
    private LabelWidget hintWidget;
    private GameContext context;

    @Override
    protected void init(GameContext context) {
        this.context = context;

        details = new ScrollableContainerWidget(41, 0, Math.max(40, getWidth() - 41), getHeight() - 1, 0);
        details.setKeyboardInput(false);
        details.setOnlyScrollOnOverflow(true);

        details.addChild(new LabelWidget(41, 0, "Select a quest to see details!"));
        details.addChild(new LabelWidget(41, 1, "--------------------------------"));
        details.addChild(new ChatMessageWidget(41, 2, 40, "Press [Space] to select a quest and view details!"));
        addChild(details);

        hintWidget = new LabelWidget(0, getHeight() - 1, "Press [Escape] to return to the game");
        addChild(hintWidget);

        drawQuests();
    }

    public void drawQuests() {
        if (options != null) {
            removeChild(options);
        }

        options = new OptionsWidget(0, 0, 40, getHeight() - 1);

        if (context.getQuests().isEmpty()) {
            options.addOption(new Option("----- No Active Quests -----", 0x909090, null));
        } else {
            for (Quest quest : context.getQuests()) {
                String title = quest.getTitle() + " (" + quest.getProgress() + "/" + quest.getRequired() + ")";
                options.addOption(new QuestOption(
                        title,
                        quest.isComplete() ? Color.GREEN : 0xf3cf83,
                        quest,
                        () -> showdetails(quest)
                ));
            }
        }

        addChild(options);
    }

    public void showdetails(Quest quest) {
        details.clearChildren();

        details.addChild(new LabelWidget(quest.getTitle(), 0xf3cf83));
        details.addChild(new LabelWidget("-".repeat(Math.max(1, quest.getTitle().length()))));
        details.addChild(new LabelWidget("Status: " + (quest.isComplete() ? "Completed" : "In Progress"), quest.isComplete() ? Color.GREEN : Color.YELLOW));
        details.addChild(new LabelWidget("Progress: " + quest.getProgress() + " / " + quest.getRequired()));
        details.addChild(new LabelWidget(BattleStatusWidget.calculateStatBar(quest.getProgress(), quest.getRequired(), 20), quest.isComplete() ? Color.GREEN : 0xf3cf83));
        details.addChild(new LabelWidget("Given by: " + quest.getNpc().getName()));
        details.addChild(new LabelWidget("Location: " + quest.getNpc().getLocation().getName()));

        if (quest.getReward() != null) {
            details.addChild(new LabelWidget(""));
            details.addChild(new LabelWidget("--- Rewards ---", 0x9e57b));
            if (quest.getReward().magicShards() > 0) {
                details.addChild(new LabelWidget("Magic Shards: " + quest.getReward().magicShards(), 0x00FFFF));
            }
            for (Item item : quest.getReward().items()) {
                details.addChild(new LabelWidget("Item: " + item.getName(), Color.WHITE));
            }
        }
    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isEscape()) {
            context.setScreen(new MainGameScreen());
        }
    }

    @Override
    public void onResize() {
        if (hintWidget != null) {
            hintWidget.setPosition(0, getHeight() - 1);
        }
        if (details != null) {
            details.setDimensions(Math.max(40, getWidth() - 41), getHeight() - 1);
        }
        drawQuests();
    }
}
