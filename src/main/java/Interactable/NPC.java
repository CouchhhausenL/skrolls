package main.java.Interactable;

import main.java.animation.Animation;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.item.Quality;
import main.java.screen.impl.NPCDialogScreen;

import main.java.screen.widget.impl.dialog.DialogItem;
import main.java.util.SkaParser;

import java.util.ArrayList;
import java.util.List;

public class NPC extends Interactable{
    NPCType npcType;

    private final List<DialogItem> defaultDialogItems = new ArrayList<>();
    private final List<DialogItem> interactedDialogItems = new ArrayList<>();

    public NPC(String id, String name, NPCType npcType){
        super(id, name,"talk", "to");
        this.npcType = npcType;
    }

    public void addDialogItem(DialogItem dialogItem){
        defaultDialogItems.add(dialogItem);
    }

    public void addInteractedDialog(DialogItem dialogItem){
        interactedDialogItems.add(dialogItem);
    }

    @Override
    public void onInteract(GameContext context){
        boolean alreadyInteracted = context.hasInteracted(getId());
        context.markInteracted(getId());
        List<DialogItem> dialogItems = defaultDialogItems;
        if(alreadyInteracted && !interactedDialogItems.isEmpty()){
            dialogItems = interactedDialogItems;
        }

        NPCDialogScreen screen = new NPCDialogScreen(
                npcType,
                dialogItems
        );
        context.setScreen(screen);
    }
}
