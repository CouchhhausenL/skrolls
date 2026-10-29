package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.ArrayList;
import java.util.List;

public class Winterfort extends SettlementScene {
    private final NPC dario;

    @Override
    public List<Interactable> getInteractables() {
        return List.of(dario);
    }
    protected Winterfort(GameContext gameContext) {
        super(gameContext);
        dario = new NPC("winterfort_npc_1","Dario the Backpacker",NPCType.BACKPACKER);
        dario.addDialogItem(new DialogItem("Hello"));
        dario.addDialogItem(new DialogItem("Whats Up?"));
        dario.addDialogItem(new DialogItem("Oh.. I see you are a new Adventurer!"));
        dario.addDialogItem(new DialogItem("Here, I'll give you a Item you might need", () -> {
            gameContext.addItem(ItemType.COMMON_MANA_POTION.create());

        }));
        dario.addInteractedDialog(new DialogItem("I have nothing more to tell to you."));

    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_EAST, "Gopher Byway", GopherByway::new);
        exits.addExit(Direction.EAST, "Lumberway", Lumberway::new);
        exits.addExit(Direction.SOUTH_EAST, "Winter Lane", WinterLane::new);
        exits.addExit(Direction.SOUTH_WEST, "Merchant Avenue", MerchantAvenue::new);
        exits.addExit(Direction.NORTH_WEST, "Gilded Approach", GildedApproach::new);
        exits.addExit("WinterfortCastle", "Enter Winterfort Castle", WinterfortCastle::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.NONE;
    }

    @Override
    public String getName() {
        return "Winterfort";
    }
}
