package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.scene.LocationScene;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class TheScoreGopherRoom extends LocationScene {
    private final NPC princess;

    protected TheScoreGopherRoom(GameContext gameContext) {
        super(gameContext);
        princess = new NPC("the_score_princess", "Princess of Wintefort", NPCType.QUEEN, this);
        princess.addDialogItem(new DialogItem("Damn, there's usually no one here this late.."));
        princess.addDialogItem(new DialogItem(".. I bet you've realised I'm the princess of Winterfort .."));
        princess.addDialogItem(new DialogItem("Please don't tell my parents that I am here but sometimes I need to escape the snobbish royal life."));
        princess.addDialogItem(new DialogItem("Take this as a little bribe, I don't need it.", () -> {
            gameContext.addItem(ItemType.UNCOMMON_RUBY_PENDANT.create());
        }));
        princess.addInteractedDialog(new DialogItem("Remember: Don't tell my parents about me!"));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(princess);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Leave The Score Gopher Room", "Leave the Score Gopher Way", TheScoreGopherRoom::new);

    }

    @Override
    public String getName() {
        return "The Score Gopher Room";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.NONE;
    }


}
