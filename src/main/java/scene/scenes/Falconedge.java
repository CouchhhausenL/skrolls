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

public class Falconedge extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC scout;

    public Falconedge(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("falconedge_chest_1", "Falconedge Supply Cache", Quality.RARE);

        salesman = new NPC("falconedge_salesman", "Falconedge Outfitter", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Welcome to Falconedge perched upon the ridge!"));
        salesman.addDialogItem(new DialogItem("The altitude can be perilous. Do you want to buy a Quicksilver Talisman for 50 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(50)) {
                    gameContext.addItem(ItemType.RARE_QUICKSILVER_TALISMAN.create());
                } else {
                    gameContext.markUnInteracted("falconedge_salesman");
                }
            } else {
                gameContext.markUnInteracted("falconedge_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Come back if you change your mind.";
                    }
                    if (!gameContext.hasInteracted("falconedge_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 50.";
                    }
                    return "An excellent choice! May speed protect you on the cliffs.";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("Always a pleasure to see brave travelers in Falconedge!"));

        scout = new NPC("falconedge_scout", "Scout Jeffrey", NPCType.ADVENTURER, this);
        scout.addDialogItem(new DialogItem("Greetings, traveler!"));
        scout.addDialogItem(new DialogItem("The mountain air is harsh, but our fortress above keeps the roads secure."));
        scout.addDialogItem(new DialogItem("Take this restorative flask — you'll need all your strength out there.", () -> {
            gameContext.addItem(ItemType.RARE_GREATER_HEALTH_POTION.create());
        }));
        scout.addInteractedDialog(new DialogItem("Keep your eyes sharp when wandering the highlands."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, scout);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH_EAST, "Highland Vector", HighlandVector::new);
        exits.addExit(Direction.WEST, "Highland Boulevard", HighlandBoulevard::new);
        exits.addExit("FalconedgeFortress", "Enter Falconedge Fortress", FalconedgeFortress::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Falconedge";
    }
}
