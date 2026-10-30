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

public class JuniperVale extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC ranger;

    public JuniperVale(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("juniper_vale_chest_1", "Juniper Carved Chest", Quality.RARE);

        salesman = new NPC("juniper_vale_salesman", "Juniper Peddler", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Welcome to quiet Juniper Vale, traveler!"));
        salesman.addDialogItem(new DialogItem("The mud and silt nearby breed nasty pests. Do you want to buy a Berserker's Crest for 50 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(50)) {
                    gameContext.addItem(ItemType.RARE_BERSERKER_CREST.create());
                } else {
                    gameContext.markUnInteracted("juniper_vale_salesman");
                }
            } else {
                gameContext.markUnInteracted("juniper_vale_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Take care in the wilderness.";
                    }
                    if (!gameContext.hasInteracted("juniper_vale_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 50.";
                    }
                    return "A fierce trinket for a fierce wizard! Good luck out there.";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("Always a delight having adventurers in our vale!"));

        ranger = new NPC("juniper_vale_ranger", "Cedric the Ranger", NPCType.ADVENTURER, this);
        ranger.addDialogItem(new DialogItem("Rest your feet, friend. Silt Passage and Mud Route are rough going."));
        ranger.addDialogItem(new DialogItem("Keep your staff ready and don't let swamp beasts flank you."));
        ranger.addDialogItem(new DialogItem("Here, take this flask of Greater Health — you might need it on the road.", () -> {
            gameContext.addItem(ItemType.RARE_GREATER_HEALTH_POTION.create());
        }));
        ranger.addInteractedDialog(new DialogItem("May the road rise up to meet you."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, ranger);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_EAST, "Silt Passage", SiltPassage::new);
        exits.addExit(Direction.SOUTH_WEST, "Mud Route", MudRoute::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "JuniperVale";
    }
}
