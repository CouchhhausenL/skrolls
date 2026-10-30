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

public class Musterfield extends SettlementScene {
    private final Chest chest;
    private final NPC quartermaster;
    private final NPC captain;

    public Musterfield(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("musterfield_chest_1", "Musterfield Arms Chest", Quality.RARE);

        quartermaster = new NPC("musterfield_quartermaster", "Musterfield Quartermaster", NPCType.SALESMAN, this);
        quartermaster.addDialogItem(new DialogItem("Attention recruit! You've arrived at the muster camp."));
        quartermaster.addDialogItem(new DialogItem("Our steel plate armor turns aside blades and claws alike. Do you want to buy Knight's Steelplate for 60 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (quartermaster.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(60)) {
                    gameContext.addItem(ItemType.RARE_KNIGHT_PLATE.create());
                } else {
                    gameContext.markUnInteracted("musterfield_quartermaster");
                }
            } else {
                gameContext.markUnInteracted("musterfield_quartermaster");
            }
        }));
        quartermaster.addDialogItem(new DialogItem(
                () -> {
                    if (!quartermaster.getEnteredBoolean()) {
                        return "Stay sharp then, mage.";
                    }
                    if (!gameContext.hasInteracted("musterfield_quartermaster")) {
                        return "You don't have enough Magic Crystals! Come back when you have 60.";
                    }
                    return "Solid steel will keep you in one piece! Dismissed!";
                },
                AnswerType.NONE,
                () -> {}
        ));
        quartermaster.addInteractedDialog(new DialogItem("Keep your gear polished and your wits sharper!"));

        captain = new NPC("musterfield_captain", "Captain Vance", NPCType.ADVENTURER, this);
        captain.addDialogItem(new DialogItem("Hail! Rowan Avenue has been plagued by erratic Frost Jackalopes freezing our scouts."));
        captain.addDialogItem(new DialogItem("Can you eliminate 3 Frost Jackalopes to clear our patrol paths?", AnswerType.BOOLEAN, () -> {
            if (captain.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "musterfield_quest_kill_jackalopes",
                        "Hunt 3 Frost Jackalopes",
                        3,
                        new QuestReward(50, ItemType.RARE_BLAZING_STAFF.create()),
                        captain,
                        MonsterType.FROST_JACKALOPE
                ));
            } else {
                gameContext.markUnInteracted("musterfield_captain");
            }
        }));
        captain.addDialogItem(new DialogItem(
                () -> captain.getEnteredBoolean() ? "Good hunting, wizard! Melt those frosty pests." : "Our patrols will just have to endure.",
                AnswerType.NONE,
                () -> {}
        ));
        captain.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("musterfield_quest_kill_jackalopes");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("musterfield_reward_claimed")) {
                            gameContext.markInteracted("musterfield_reward_claimed");
                            gameContext.applyQuestReward(quest.getReward());
                            return "Excellent marksmanship! The roads are safe from frost. Take this Blazing Firestaff as your well-earned reward!";
                        }
                        return "The muster camp salutes your service!";
                    } else if (quest != null) {
                        return "You've hunted " + quest.getProgress() + " / " + quest.getRequired() + " Frost Jackalopes so far.";
                    }
                    return "Stay disciplined out there.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, quartermaster, captain);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH_WEST, "Rowan Avenue", RowanAvenue::new);
        exits.addExit(Direction.EAST, "Rowan Connector", RowanConnector::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Musterfield";
    }
}
