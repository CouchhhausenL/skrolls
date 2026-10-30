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

public class SaplingFord extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC lyna;

    public SaplingFord(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("sapling_ford_chest_1", "Hollow Stump Cache", Quality.UNCOMMON);

        salesman = new NPC("sapling_ford_salesman", "River Peddler", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Welcome to Sapling Ford! The river flows peaceful, but danger lurks in Acorn Lane."));
        salesman.addDialogItem(new DialogItem("Do you want to buy a Ruby Heart Pendant for 20 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(20)) {
                    gameContext.addItem(ItemType.UNCOMMON_RUBY_PENDANT.create());
                } else {
                    gameContext.markUnInteracted("sapling_ford_salesman");
                }
            } else {
                gameContext.markUnInteracted("sapling_ford_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Watch the waters as you cross.";
                    }
                    if (!gameContext.hasInteracted("sapling_ford_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 20.";
                    }
                    return "Its warmth will bolster your spirit! Safe travels.";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("Always a pleasure doing business at the ford!"));

        lyna = new NPC("sapling_ford_lyna", "Nimble Lyna", NPCType.ELF, this);
        lyna.addDialogItem(new DialogItem("Greetings, traveler! The young saplings here need peaceful soil to grow."));
        lyna.addDialogItem(new DialogItem("Rowdy Orks have been trampling the riverside flora.\nCould you repel 3 Orks to protect the ford?", AnswerType.BOOLEAN, () -> {
            if (lyna.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "sapling_ford_quest_kill_orks",
                        "Repel 3 Orks",
                        3,
                        new QuestReward(35, ItemType.UNCOMMON_ZEPHYR_CHARM.create()),
                        lyna,
                        MonsterType.ORK
                ));
            } else {
                gameContext.markUnInteracted("sapling_ford_lyna");
            }
        }));
        lyna.addDialogItem(new DialogItem(
                () -> lyna.getEnteredBoolean() ? "May the river currents speed your spells! Thank you!" : "Then we must tread softly.",
                AnswerType.NONE,
                () -> {}
        ));
        lyna.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("sapling_ford_quest_kill_orks");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("sapling_ford_reward_claimed")) {
                            gameContext.markInteracted("sapling_ford_reward_claimed");
                            gameContext.applyQuestReward(quest.getReward());
                            return "The saplings flourish again! Thank you, kind traveler. Take this Zephyr Charm as our gift!";
                        }
                        return "The river runs clear and quiet thanks to you.";
                    } else if (quest != null) {
                        return "You've repelled " + quest.getProgress() + " / " + quest.getRequired() + " Orks.";
                    }
                    return "Peace be with your journey.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, lyna);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_EAST, "Winterfort", Winterfort::new);
        exits.addExit(Direction.SOUTH_WEST, "Acorn Lane", AcornLane::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

    @Override
    public String getName() {
        return "Sapling Ford";
    }
}
