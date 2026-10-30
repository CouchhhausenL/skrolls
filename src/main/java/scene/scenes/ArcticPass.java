package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.scene.Direction;
import main.java.scene.PathScene;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class ArcticPass extends PathScene {
    private final NPC wanderer;

    public ArcticPass(GameContext gameContext) {
        super(gameContext);
        wanderer = new NPC("arctic_pass_wanderer", "Kael the Stray", NPCType.ADVENTURER, this);
        wanderer.addDialogItem(new DialogItem("B-brrr! The biting gale through this gorge nearly froze my boots to the ice!"));
        wanderer.addDialogItem(new DialogItem("It's a miracle to see another soul alive in this blizzard."));
        wanderer.addDialogItem(new DialogItem("Take this Warding Talisman — its protective magic kept me alive when the frost struck.", () -> {
            gameContext.addItem(ItemType.UNCOMMON_WARDING_TALISMAN.create());
        }));
        wanderer.addInteractedDialog(new DialogItem("Keep moving, traveler! Don't let the frost claim your limbs."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(wanderer);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH, "Faywater", Faywater::new);
        exits.addExit(Direction.NORTH, "Arctic-Boulder Intersection", ArcticBoulderIntersection::new);
    }

    @Override
    public String getName() {
        return "Arctic Pass";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
