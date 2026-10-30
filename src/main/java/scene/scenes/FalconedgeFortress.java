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

public class FalconedgeFortress extends LocationScene {
    private final Chest chest;
    private final NPC king;

    public FalconedgeFortress(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("falconedge_fortress_chest_1", "Ironbound Fortress Chest", Quality.EPIC);
        king = new NPC("falconedge_fortress_king", "King Gerald of Falconedge", NPCType.KING, this);

        king.addDialogItem(new DialogItem("Hail, spellweaver! You stand in the high halls of Falconedge Fortress."));
        king.addDialogItem(new DialogItem("Hostile Pure Gryphons swoop down from the alpine peaks, harassing our archers and supply lines."));
        king.addDialogItem(new DialogItem("Can you bring down 3 Pure Gryphons to defend our skyward bastion?", AnswerType.BOOLEAN, () -> {
            if (king.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "falconedge_fortress_quest_kill_gryphons",
                        "Hunt 3 Pure Gryphons",
                        3,
                        new QuestReward(80, ItemType.EPIC_ARCHON_SCEPTER.create()),
                        king,
                        MonsterType.PURE_GRYPHON
                ));
            } else {
                gameContext.markUnInteracted("falconedge_fortress_king");
            }
        }));

        king.addDialogItem(new DialogItem(
                () -> king.getEnteredBoolean()
                        ? "May the high winds guide your spells! Return once the skies are cleared."
                        : "A pity. We shall endure as we always have — through blood and iron.",
                AnswerType.NONE,
                () -> {}
        ));

        king.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("falconedge_fortress_quest_kill_gryphons");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("falconedge_fortress_reward_claimed")) {
                            gameContext.markInteracted("falconedge_fortress_reward_claimed");
                            gameContext.applyQuestReward(quest.getReward());
                            return "A magnificent victory! The skies belong to Falconedge once more. Accept this Archon Scepter as our token of honor!";
                        }
                        return "The skies remain calm under your watchful deeds, wizard.";
                    } else if (quest != null) {
                        return "You have brought down " + quest.getProgress() + " out of " + quest.getRequired() + " Pure Gryphons. Keep hunting!";
                    }
                    return "Falconedge stands vigilant.";
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
        exits.addExit("Falconedge", "Leave Falconedge Fortress", Falconedge::new);
    }

    @Override
    public String getName() {
        return "Falconedge Fortress";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
