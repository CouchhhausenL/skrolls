package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.item.Quality;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class Faywater extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC sylvia;

    public Faywater(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("faywater_chest_1", "Faywater Coral Coffer", Quality.RARE);

        salesman = new NPC("faywater_salesman", "Faywater Harbor Merchant", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Ahoy! The salty sea breeze brings rare goods to Faywater's docks."));
        salesman.addDialogItem(new DialogItem("Looking for protection against coastal beasts? Do you want to buy an Aegis Pendant for 50 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(50)) {
                    gameContext.addItem(ItemType.RARE_AEGIS_PENDANT.create());
                } else {
                    gameContext.markUnInteracted("faywater_salesman");
                }
            } else {
                gameContext.markUnInteracted("faywater_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Farewell, traveler.";
                    }
                    if (!gameContext.hasInteracted("faywater_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 50.";
                    }
                    return "May fair winds and tides carry you safely!";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("The tides bring good fortune to generous patrons!"));

        sylvia = new NPC("faywater_sylvia", "Sylvia of the Tides", NPCType.ELF, this);
        sylvia.addDialogItem(new DialogItem("Greetings, wanderer. The sea sings ancient songs if you listen closely."));
        sylvia.addDialogItem(new DialogItem("Ships departing for the Abandoned Isle face harsh tempests and dreadful beasts."));
        sylvia.addDialogItem(new DialogItem("Accept this Greater Health Flask — keep your vitality strong on the waves.", () -> {
            gameContext.addItem(ItemType.RARE_GREATER_HEALTH_POTION.create());
        }));
        sylvia.addInteractedDialog(new DialogItem("May the ocean currents keep your spirit afloat."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, sylvia);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Seaside Route", SeasideRoute::new);
        exits.addExit(Direction.NORTH, "Artic Pass", ArcticPass::new);
        exits.addExit("TheAbandinedIsle", "Ship to The Abandined Isle", TheAbandinedIsle::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Faywater";
    }
}
