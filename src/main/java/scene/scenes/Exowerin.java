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

public class Exowerin extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC eldrin;

    public Exowerin(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("exowerin_chest_1", "Enchanted Elven Chest", Quality.LEGENDARY);

        salesman = new NPC("exowerin_salesman", "Exowerin Arcane Merchant", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Greetings, walker of the mystical currents! Exowerin welcomes enlightened minds."));
        salesman.addDialogItem(new DialogItem("Do you wish to acquire an Astral Prism Staff for 100 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(100)) {
                    gameContext.addItem(ItemType.EPIC_ASTRAL_PILLAR.create());
                } else {
                    gameContext.markUnInteracted("exowerin_salesman");
                }
            } else {
                gameContext.markUnInteracted("exowerin_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "The stars wait patiently.";
                    }
                    if (!gameContext.hasInteracted("exowerin_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 100.";
                    }
                    return "May the cosmos guide each spell you weave!";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("May tranquility follow your steps in Exowerin."));

        eldrin = new NPC("exowerin_eldrin", "Arch-Mage Eldrin", NPCType.ELF, this);
        eldrin.addDialogItem(new DialogItem("Peace be upon you, traveler. Our elder shrines are disturbed."));
        eldrin.addDialogItem(new DialogItem("Corrupted Felid Spellweavers are distorting the ley lines around Redwood Woodlands.\nWill you defeat 2 Felid Spellweavers to restore harmony?", AnswerType.BOOLEAN, () -> {
            if (eldrin.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "exowerin_quest_kill_spellweavers",
                        "Defeat 2 Felid Spellweavers",
                        2,
                        new QuestReward(120, ItemType.LEGENDARY_ASTRAL_STAFF.create()),
                        eldrin,
                        MonsterType.FELID_SPELLWEAVER
                ));
            } else {
                gameContext.markUnInteracted("exowerin_eldrin");
            }
        }));
        eldrin.addDialogItem(new DialogItem(
                () -> eldrin.getEnteredBoolean() ? "Thank you, noble wizard. Cleanse the ley lines with dignity." : "Then the ancient trees must weather the storm alone.",
                AnswerType.NONE,
                () -> {}
        ));
        eldrin.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("exowerin_quest_kill_spellweavers");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("exowerin_reward_claimed")) {
                            gameContext.markInteracted("exowerin_reward_claimed");
                            gameContext.applyQuestReward(quest);
                            return "The ley lines sing in harmony once more! Receive this Staff of the Cosmos as a symbol of our eternal gratitude!";
                        }
                        return "The astral balance is serene thanks to your deeds.";
                    } else if (quest != null) {
                        return "You have pacified " + quest.getProgress() + " / " + quest.getRequired() + " Felid Spellweavers.";
                    }
                    return "May the starlight guide you.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, eldrin);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_EAST, "Archer Passage", ArcherPassage::new);
        exits.addExit("TheYggdrasoul", "Visit The Yggdrasoul Elder Tree", TheYggdrasoul::new);
        exits.addExit("Redwood Woodlands", "Go to surrounding Redwood Woodlands", RedwoodWoodlands::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }

    @Override
    public String getName() {
        return "Exowerin";
    }
}
