package main.java.screen.impl;

import main.java.Interactable.NPCType;
import main.java.animation.Animation;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.dialog.DialogItem;
import main.java.util.SkaParser;

import java.util.List;
public class NPCDialogScreen extends Screen {

    private DialogWidget dialogWidget;
    private final NPCType npcType;
    private AnimationWidget idleAnimation;
    private AnimationWidget talkAnimation;
    private final List<DialogItem> items;

    public NPCDialogScreen(NPCType npcType, List<DialogItem> items) {
        this.npcType = npcType;
        this.items = items;

    }

    @Override
    protected void init(GameContext context) {
        dialogWidget = new DialogWidget(40,0,Math.max(40,(getWidth()-40)),30);
        for (DialogItem item : items) {
            this.dialogWidget.addDialogItem(item);
        }
        idleAnimation = new AnimationWidget(0, 0, SkaParser.parse("character/npc/" + npcType.name().toLowerCase() + "/idle.ska"));
        idleAnimation.setLooping(true);

        talkAnimation = new AnimationWidget(0, 0, SkaParser.parse("character/npc/" + npcType.name().toLowerCase() + "/talk.ska"));
        addChild(talkAnimation);
        talkAnimation.onDone(() -> {
            removeChild(talkAnimation);
            addChild(idleAnimation);
        });

        dialogWidget.setOnEachAnswer(() -> {
            removeChild(idleAnimation);
            talkAnimation.reset();
            addChild(talkAnimation);
            talkAnimation.onDone(() -> {
                removeChild(talkAnimation);
                addChild(idleAnimation);
            });
        });
        dialogWidget.setOnComplete(() -> context.setScreen(new MainGameScreen()));

        addChild(dialogWidget);
    }


    public boolean getEnteredBoolean(){
        return dialogWidget.getEnteredBoolean();
    }

    @Override
    public void onResize() {

    }
}