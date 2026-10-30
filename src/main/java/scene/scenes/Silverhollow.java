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

public class Silverhollow extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC elen;

    public Silverhollow(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("silverhollow_chest_1", "Silver-Lined Coffer", Quality.RARE);

        salesman = new NPC("silverhollow_salesman", "Silverhollow Lapidary", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Greetings, traveler! Silverhollow craftsmen cut the finest arcane facets."));
        salesman.addDialogItem(new DialogItem("Do you want to buy an Elemental Heartstone for 50 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(50)) {
                    gameContext.addItem(ItemType.RARE_ELEMENTAL_CORE.create());
                } else {
                    gameContext.markUnInteracted("silverhollow_salesman");
                }
            } else {
                gameContext.markUnInteracted("silverhollow_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Come again whenever your curiosity beckons.";
                    }
                    if (!gameContext.hasInteracted("silverhollow_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 50.";
                    }
                    return "May its inner fire illuminate your path!";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("Silverhollow treasures always retain their luster!"));

        elen = new NPC("silverhollow_elen", "Priestess Elen", NPCType.ELF, this);
        elen.addDialogItem(new DialogItem("May the holy light preserve you, wanderer."));
        elen.addDialogItem(new DialogItem("Vile Half Blood Demons from the abyss have crept into the southern passes, defiling sacred groves.\nWill you banish 3 Half Blood Demons in the name of purity?", AnswerType.BOOLEAN, () -> {
            if (elen.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "silverhollow_quest_kill_demons",
                        "Banish 3 Half Blood Demons",
                        3,
                        new QuestReward(60, ItemType.RARE_RADIANT_STAFF.create()),
                        elen,
                        MonsterType.HALF_BLOOD_DEMON
                ));
            } else {
                gameContext.markUnInteracted("silverhollow_elen");
            }
        }));
        elen.addDialogItem(new DialogItem(
                () -> elen.getEnteredBoolean() ? "Blessings upon your mission! Cast the darkness back into the abyss." : "Then we must pray for another champion.",
                AnswerType.NONE,
                () -> {}
        ));
        elen.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("silverhollow_quest_kill_demons");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("silverhollow_reward_claimed")) {
                            gameContext.markInteracted("silverhollow_reward_claimed");
                            gameContext.applyQuestReward(quest.getReward());
                            return "The light triumphs! The groves are cleansed. Bear this Radiant Lightstaff with pride, champion!";
                        }
                        return "The silver sanctuaries remain pure under your protection.";
                    } else if (quest != null) {
                        return "You have banished " + quest.getProgress() + " / " + quest.getRequired() + " Half Blood Demons.";
                    }
                    return "Walk with grace.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, elen);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Granit Route", GranitRoute::new);
        exits.addExit(Direction.SOUTH, "Ivory Pass", IvoryPass::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Silverhollow";
    }
}
