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

public class MerchantAvenue extends PathScene {
    private final NPC backpacker;

    public MerchantAvenue(GameContext gameContext) {
        super(gameContext);
        backpacker = new NPC("merchant_avenue_backpacker", "Finneas the Backpacker", NPCType.BACKPACKER, this);
        backpacker.addDialogItem(new DialogItem("Phew! Rest a moment by the milestone, stranger."));
        backpacker.addDialogItem(new DialogItem("Traveling between Winterfort and Sapling Ford is heavy on the soles."));
        backpacker.addDialogItem(new DialogItem("I have a spare Buckler Charm tucked in my pack. Would you like to trade for it?", AnswerType.BOOLEAN, () -> {
            if (backpacker.getEnteredBoolean()) {
                gameContext.addItem(ItemType.COMMON_GUARD_CHARM.create());
            } else {
                gameContext.markUnInteracted("merchant_avenue_backpacker");
            }
        }));
        backpacker.addDialogItem(new DialogItem(
                () -> backpacker.getEnteredBoolean() ? "Pleasure trading with ye! Safe travels down the avenue." : "Fair enough. May the road treat you well.",
                AnswerType.NONE,
                () -> {}
        ));
        backpacker.addInteractedDialog(new DialogItem("Taking a short breather before I head down to the ford!"));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(backpacker);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_EAST, "Winterfort", Winterfort::new);
        exits.addExit(Direction.SOUTH_WEST, "Sapling Ford", SaplingFord::new);
    }

    @Override
    public String getName() {
        return "Merchant Avenue";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }
}
