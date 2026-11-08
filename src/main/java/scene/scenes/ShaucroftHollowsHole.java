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

public class ShaucroftHollowsHole extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;


    public ShaucroftHollowsHole(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("shaucroft_chest_1", "Hollows Runed Chest", Quality.RARE);

        salesman = new NPC("shaucroft_salesman", "Hollows Trinket Trader", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Psst! Step out of the mist into the hollows."));
        salesman.addDialogItem(new DialogItem("I've unearthed a Gravebinder Gem that repels necrotic curses. Do you want to buy it for 50 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(50)) {
                    gameContext.addItem(ItemType.RARE_SOUL_GEM.create());
                } else {
                    gameContext.markUnInteracted("shaucroft_salesman");
                }
            } else {
                gameContext.markUnInteracted("shaucroft_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Watch the shadows then.";
                    }
                    if (!gameContext.hasInteracted("shaucroft_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 50.";
                    }
                    return "Keep it close when the mists thicken. Good fortune!";
                },
                AnswerType.NONE,
                () -> {
                }
        ));
        salesman.addInteractedDialog(new DialogItem("Come back if you need more obscure artifacts!"));

    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Leave Shaucroft Hollows Hole", "Leave Shaucroft Hollows Hole", ShaucroftHollows::new);

    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Shaucroft Hollows";
    }
}
