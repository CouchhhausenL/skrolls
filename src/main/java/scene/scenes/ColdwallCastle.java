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
import main.java.scene.LocationScene;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class ColdwallCastle extends LocationScene {
    private final Chest chest;
    private final NPC king;

    public ColdwallCastle(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("coldwall_castle_chest_1", "Gilded Royal Chest", Quality.EPIC);
        king = new NPC("coldwall_castle_king", "King Aldous of Coldwall", NPCType.KING, this);

        king.addDialogItem(new DialogItem("Welcome to Coldwall Castle, wizard traveler."));
        king.addDialogItem(new DialogItem("Our icy ramparts are besieged by shadowy terrors from the nether realms.\nThe Grim Reapers haunt our outer walls and strike fear into the hearts of my guards."));
        king.addDialogItem(new DialogItem("Will you slay 3 Grim Reapers to safeguard our realm?", AnswerType.BOOLEAN, () -> {
            if (king.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "coldwall_castle_king_quest_kill_reapers",
                        "Purge 3 Grim Reapers",
                        3,
                        new QuestReward(80, ItemType.EPIC_SANCTIFIED_CUIRASS.create()),
                        king,
                        MonsterType.GRIM_REAPER
                ));
            } else {
                gameContext.markUnInteracted("coldwall_castle_king");
            }
        }));

        king.addDialogItem(new DialogItem(
                () -> king.getEnteredBoolean()
                        ? "The gratitude of the northern throne goes with you! Return when the reapers are slain."
                        : "Then we must rely on our own steel. May the winter spare your insolence.",
                AnswerType.NONE,
                () -> {}
        ));

        king.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("coldwall_castle_king_quest_kill_reapers");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("coldwall_castle_reward_claimed")) {
                            gameContext.markInteracted("coldwall_castle_reward_claimed");
                            gameContext.applyQuestReward(quest.getReward());
                            return "Splendid bravery! The Grim Reapers are banished. Take this enchanted cuirass and our royal favor!";
                        }
                        return "Coldwall stands strong thanks to your valor, wizard!";
                    } else if (quest != null) {
                        return "You have vanquished " + quest.getProgress() + " out of " + quest.getRequired() + " Grim Reapers. Stand resolute!";
                    }
                    return "Greetings from Coldwall Castle.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, king);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Kronomore", "Leave Castle, back to City", Kronomore::new);
    }

    @Override
    public String getName() {
        return "Coldwall Castle";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
