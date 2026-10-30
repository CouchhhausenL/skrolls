package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.item.Quality;
import main.java.scene.SettlementScene;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class TheAbandinedIsle extends SettlementScene {
    private final Chest chest;
    private final NPC smuggler;
    private final NPC castaway;

    public TheAbandinedIsle(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("abandoned_isle_chest_1", "Sunken Pirate Vault", Quality.LEGENDARY);

        smuggler = new NPC("abandoned_isle_smuggler", "Shadow Smuggler", NPCType.SALESMAN, this);
        smuggler.addDialogItem(new DialogItem("Heh... Few daring souls set foot on this forgotten rock."));
        smuggler.addDialogItem(new DialogItem("I salvage dark relics from sunken galleons. Fancy buying a Null-Void Shroud to swallow abyssal curses?", AnswerType.BOOLEAN, () -> {
            if (smuggler.getEnteredBoolean()) {
                gameContext.addItem(ItemType.EPIC_VOID_SHROUD.create());
            } else {
                gameContext.markUnInteracted("abandoned_isle_smuggler");
            }
        }));
        smuggler.addDialogItem(new DialogItem(
                () -> smuggler.getEnteredBoolean() ? "Keep it out of the sunlight, friend. Pleasure doing business." : "Don't let the sea swallow ye then.",
                AnswerType.NONE,
                () -> {}
        ));
        smuggler.addInteractedDialog(new DialogItem("No questions asked, no secrets spilled."));

        castaway = new NPC("abandoned_isle_castaway", "Barnaby the Castaway", NPCType.BACKPACKER, this);
        castaway.addDialogItem(new DialogItem("By the tides! A real, living wizard in the flesh!"));
        castaway.addDialogItem(new DialogItem("I've been stranded on these coral shores for longer than I can remember."));
        castaway.addDialogItem(new DialogItem("I found this Adamantine Draft in the wreckage. Take it, you need it far more than an old sailor!", () -> {
            gameContext.addItem(ItemType.LEGENDARY_AEGIS_ELIXIR.create());
        }));
        castaway.addInteractedDialog(new DialogItem("If you find a ship sailing home, save a hammock for Barnaby!"));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, smuggler, castaway);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Faywater", "Ship back to Faywater", Faywater::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }

    @Override
    public String getName() {
        return "The Abandined Isle";
    }
}
