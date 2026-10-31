package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.entity.monster.MonsterType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.item.Quality;
import main.java.quest.Quest;
import main.java.quest.QuestReward;
import main.java.quest.impl.KillQuest;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class Deepforge extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC kingThror;

    public Deepforge(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("deepforge_vault_1", "Deepforge Vault Chest", Quality.LEGENDARY);

        salesman = new NPC("deepforge_salesman", "Deepforge Master Armorer", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Welcome to the molten depths of Deepforge!"));
        salesman.addDialogItem(new DialogItem("Here we temper equipment in subterranean magma. Do you want to buy Ancient Wyrmscale Mail for 100 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(100)) {
                    gameContext.addItem(ItemType.EPIC_WYRMSCALE_MAIL.create());
                } else {
                    gameContext.markUnInteracted("deepforge_salesman");
                }
            } else {
                gameContext.markUnInteracted("deepforge_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "No matter. The lava never cools anyway.";
                    }
                    if (!gameContext.hasInteracted("deepforge_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 100.";
                    }
                    return "Forged in true dragon flame! It shall serve you well.";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("May your courage remain as unyielding as mithril!"));

        kingThror = new NPC("deepforge_king_thror", "Forgemaster Thror", NPCType.DWARF, this);
        kingThror.addDialogItem(new DialogItem("Stand tall, wizard! You walk the ancestral halls of Deepforge."));
        kingThror.addDialogItem(new DialogItem("Winged beasts nest in the magma flues above our smelters.\nCan you slay 2 Lesser Wyverns to keep our great furnaces breathing?", AnswerType.BOOLEAN, () -> {
            if (kingThror.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "deepforge_quest_kill_wyverns",
                        "Slay 2 Lesser Wyverns",
                        2,
                        new QuestReward(120, ItemType.LEGENDARY_TITAN_BULWARK.create()),
                        kingThror,
                        MonsterType.LESSER_WYVERN
                ));
            } else {
                gameContext.markUnInteracted("deepforge_king_thror");
            }
        }));
        kingThror.addDialogItem(new DialogItem(
                () -> kingThror.getEnteredBoolean() ? "Bring them down from the magma plumes! Honor to your house." : "Then our smiths shall take up heavy crossbows ourselves.",
                AnswerType.NONE,
                () -> {}
        ));
        kingThror.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("deepforge_quest_kill_wyverns");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("deepforge_reward_claimed")) {
                            gameContext.markInteracted("deepforge_reward_claimed");
                            gameContext.applyQuestReward(quest);
                            return "A titanic feat! The flues are cleared! Take this Dread Titan Bulwark as the highest honor of Deepforge!";
                        }
                        return "Deepforge's flames burn bright in your name, grand wizard!";
                    } else if (quest != null) {
                        return "You've vanquished " + quest.getProgress() + " / " + quest.getRequired() + " Lesser Wyverns.";
                    }
                    return "The mountain bows to none.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, kingThror);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Lumberhighs-Deepforge Crossing", LumberhighsDeepforgeCrossing::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }

    @Override
    public String getName() {
        return "Deepforge";
    }
}
