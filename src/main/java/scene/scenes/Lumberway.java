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

public class Lumberway extends PathScene {
    private final NPC bram;

    public Lumberway(GameContext gameContext) {
        super(gameContext);
        bram = new NPC("lumberway_bram", "Old Bram the Woodcutter", NPCType.BACKPACKER, this);
        bram.addDialogItem(new DialogItem("Ho there! Just stacking birch logs along the verge."));
        bram.addDialogItem(new DialogItem("You look like you're heading deep into the brass woods."));
        bram.addDialogItem(new DialogItem("Here's a flask of Minor Health Potion my daughter brewed. Drink it when trouble finds you.", () -> {
            gameContext.addItem(ItemType.COMMON_HEALTH_POTION.create());
        }));
        bram.addInteractedDialog(new DialogItem("Watch out for splintering branches and hungry beasts on the road!"));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(bram);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Winterfort", Winterfort::new);
        exits.addExit(Direction.EAST, "Brass Maple Wood", BrassMapleWood::new);
    }

    @Override
    public String getName() {
        return "Lumberway";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }
}
