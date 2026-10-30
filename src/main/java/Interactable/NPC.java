package main.java.Interactable;

import main.java.game.GameContext;
import main.java.scene.Scene;
import main.java.screen.impl.NPCDialogScreen;

import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.ArrayList;
import java.util.List;

public class NPC extends Interactable{
    NPCType npcType;
    Scene location;

    NPCDialogScreen screen;

    private final List<DialogItem> defaultDialogItems = new ArrayList<>();
    private final List<DialogItem> interactedDialogItems = new ArrayList<>();

    public NPC(String id, String name, NPCType npcType, Scene location){
        super(id, name,"talk", "to");
        this.npcType = npcType;
        this.location = location;
    }

    public void addDialogItem(DialogItem dialogItem){
        defaultDialogItems.add(dialogItem);
    }

    public Boolean getEnteredBoolean(){
        return screen.getEnteredBoolean();
    }

    public Scene getLocation() {
        return location;
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

        screen = new NPCDialogScreen(
                npcType,
                dialogItems
        );
        context.setScreen(screen);
    }
}
