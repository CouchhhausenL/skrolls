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

public class Kronomore extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC guard;

    public Kronomore(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("kronomore_chest_1", "Kronomore Strongbox", Quality.UNCOMMON);

        salesman = new NPC("kronomore_salesman", "Kronomore Outfitter", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Greetings, traveler! Cold winds blow from Coldwall Castle today."));
        salesman.addDialogItem(new DialogItem("Do you want to buy a suit of Reinforced Chainmail for 25 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(25)) {
                    gameContext.addItem(ItemType.UNCOMMON_CHAINMAIL.create());
                } else {
                    gameContext.markUnInteracted("kronomore_salesman");
                }
            } else {
                gameContext.markUnInteracted("kronomore_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Safe travels then, friend.";
                    }
                    if (!gameContext.hasInteracted("kronomore_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you can afford it.";
                    }
                    return "May it protect you well against the cold and claws!";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("Pleasure doing business with you!"));

        guard = new NPC("kronomore_guard", "Gareth the Guard", NPCType.ADVENTURER, this);
        guard.addDialogItem(new DialogItem("Halt! You look capable with a staff."));
        guard.addDialogItem(new DialogItem("Zombies have been wandering out of the barrows near Cathedral Lane.\nCan you cull 4 Zombies before they reach the village gate?", AnswerType.BOOLEAN, () -> {
            if (guard.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "kronomore_quest_kill_zombies",
                        "Clear 4 Zombies",
                        4,
                        new QuestReward(35, ItemType.UNCOMMON_STEEL_BANGLE.create()),
                        guard,
                        MonsterType.ZOMBIE
                ));
            } else {
                gameContext.markUnInteracted("kronomore_guard");
            }
        }));
        guard.addDialogItem(new DialogItem(
                () -> guard.getEnteredBoolean() ? "Thank the gods! Put those walking corpses back in the dirt." : "Watch your back out there then.",
                AnswerType.NONE,
                () -> {}
        ));
        guard.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("kronomore_quest_kill_zombies");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("kronomore_reward_claimed")) {
                            gameContext.markInteracted("kronomore_reward_claimed");
                            gameContext.applyQuestReward(quest);
                            return "Great work! Kronomore sleeps safer tonight. Take this steel bangle as your reward!";
                        }
                        return "Thanks again for dealing with those zombies!";
                    } else if (quest != null) {
                        return "You've taken down " + quest.getProgress() + " / " + quest.getRequired() + " Zombies so far.";
                    }
                    return "Stay alert near the roads.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, guard);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Cathedral Lane", CathedralLane::new);
        exits.addExit(Direction.EAST, "Celestial Rill", CelestialRill::new);
        exits.addExit("ColdwallCastle", "Enter Coldwall Castle", ColdwallCastle::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }

    @Override
    public String getName() {
        return "Kronomore";
    }
}
