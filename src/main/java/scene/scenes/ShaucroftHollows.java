package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.entity.monster.MonsterType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.quest.Quest;
import main.java.quest.QuestReward;
import main.java.quest.impl.KillQuest;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class ShaucroftHollows extends SettlementScene {


    private final NPC brog;

    public ShaucroftHollows(GameContext gameContext) {
        super(gameContext);


        brog = new NPC("shaucroft_brog", "Brog the Excavator", NPCType.DWARF, this);
        brog.addDialogItem(new DialogItem("Bah! Can't dig two feet in these hollows without hearing rattling bones!"));
        brog.addDialogItem(new DialogItem("A company of Skeletons has taken over our trench line.\nSmash 4 Skeletons for me so I can get back to mining!", AnswerType.BOOLEAN, () -> {
            if (brog.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "shaucroft_quest_kill_skeletons",
                        "Smash 4 Skeletons",
                        4,
                        new QuestReward(50, ItemType.RARE_BERSERKER_CREST.create()),
                        brog,
                        MonsterType.SKELETON
                ));
            } else {
                gameContext.markUnInteracted("shaucroft_brog");
            }
        }));
        brog.addDialogItem(new DialogItem(
                () -> brog.getEnteredBoolean() ? "Crack their skulls! Show 'em dwarven-level might!" : "Then go tip-toe around the bones yourself.",
                AnswerType.NONE,
                () -> {}
        ));
        brog.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("shaucroft_quest_kill_skeletons");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("shaucroft_reward_claimed")) {
                            gameContext.markInteracted("shaucroft_reward_claimed");
                            gameContext.applyQuestReward(quest);
                            return "Ha! Nothing beats the sound of splintering bone! Take this Berserker's Crest as your cut of the bounty!";
                        }
                        return "The digging goes smoothly thanks to you, friend!";
                    } else if (quest != null) {
                        return "Still " + (quest.getRequired() - quest.getProgress()) + " rattling boneheads left to smash.";
                    }
                    return "Keep your boots out of the mud.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(brog);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.SOUTH_WEST, "Meadow Trace", MeadowTrace::new);
        exits.addExit(Direction.EAST, "Meadow Trodden Path", MeadowTroddenPath::new);
        exits.addExit("Enter Shaucraft Hollows Hole", "Enter Shaucroft Hollows Hole", ShaucroftHollowsHole::new);
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
