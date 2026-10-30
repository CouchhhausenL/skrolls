package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.scene.Direction;
import main.java.scene.PathScene;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class SeasideRoute extends PathScene {
    private final NPC beachcomber;

    public SeasideRoute(GameContext gameContext) {
        super(gameContext);
        beachcomber = new NPC("seaside_beachcomber", "Drake the Beachcomber", NPCType.ADVENTURER, this);
        beachcomber.addDialogItem(new DialogItem("Listen to the waves crash against the shingle. Peace before the storm."));
        beachcomber.addDialogItem(new DialogItem("I salvaged an Abyssal Chitin Carapace washed ashore from deep waters. Care to trade for it?", AnswerType.BOOLEAN, () -> {
            if (beachcomber.getEnteredBoolean()) {
                gameContext.addItem(ItemType.RARE_CHITIN_ARMOR.create());
            } else {
                gameContext.markUnInteracted("seaside_beachcomber");
            }
        }));
        beachcomber.addDialogItem(new DialogItem(
                () -> beachcomber.getEnteredBoolean() ? "Sturdy plating! Resistant to the salty brine. May it protect you!" : "May the coastal breezes favor your walk.",
                AnswerType.NONE,
                () -> {}
        ));
        beachcomber.addInteractedDialog(new DialogItem("Still sifting the sands for forgotten trinkets."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(beachcomber);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Winterfort", Winterfort::new);
        exits.addExit(Direction.EAST, "Faywater", Faywater::new);
    }

    @Override
    public String getName() {
        return "Seaside Route";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
