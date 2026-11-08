package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class Ditchgate extends SettlementScene {

    private final NPC salesman;


    public Ditchgate(GameContext gameContext) {
        super(gameContext);


        salesman = new NPC("ditchgate_salesman", "Ditchgate Sellsman", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Greetings!"));
        salesman.addDialogItem(new DialogItem("Do you want to acquire a Epic Warping Caduceus Staff for 80 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(80)) {
                    gameContext.addItem(ItemType.EPIC_WARPING_CADUCEUS.create());
                } else {
                    gameContext.markUnInteracted("ditchgate_salesman");
                }
            } else {
                gameContext.markUnInteracted("ditchgate_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Thank you!";
                    }
                    if (!gameContext.hasInteracted("exowerin_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 80.";
                    }
                    return "It was nice doing business with you!";
                },
                AnswerType.NONE,
                () -> {
                }
        ));
        salesman.addInteractedDialog(new DialogItem("May tranquility follow your steps in Exowerin."));

    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(salesman);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_EAST, "Clioppexar", Clioppexar::new);
        exits.addExit(Direction.SOUTH_WEST, "Saphire Way", SaphireWay::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Ditchgate";
    }
}
