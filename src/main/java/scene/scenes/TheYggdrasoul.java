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

public class TheYggdrasoul extends LocationScene {
    private final Chest chest;
    private final NPC elder;

    public TheYggdrasoul(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("yggdrasoul_chest_1", "Yggdrasoul Root Reliquary", Quality.LEGENDARY);

        elder = new NPC("yggdrasoul_elder", "Elder Alistair", NPCType.ELF, this);
        elder.addDialogItem(new DialogItem("You stand beneath the boughs of The Yggdrasoul, the world's beating pulse."));
        elder.addDialogItem(new DialogItem("A cataclysmic Chaos Abomination is tearing at the ethereal root network.\nWill you strike down the Chaos Abomination to preserve the world tree?", AnswerType.BOOLEAN, () -> {
            if (elder.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "yggdrasoul_quest_kill_chaos",
                        "Subdue the Chaos Abomination",
                        1,
                        new QuestReward(150, ItemType.LEGENDARY_VOID_SINGULARITY_HEART.create()),
                        elder,
                        MonsterType.CHAOS_ABOMINATION
                ));
            } else {
                gameContext.markUnInteracted("yggdrasoul_elder");
            }
        }));
        elder.addDialogItem(new DialogItem(
                () -> elder.getEnteredBoolean() ? "May the world tree's roots steady your soul in combat." : "Then nature must mourn as chaos consumes.",
                AnswerType.NONE,
                () -> {}
        ));
        elder.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("yggdrasoul_quest_kill_chaos");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("yggdrasoul_reward_claimed")) {
                            gameContext.markInteracted("yggdrasoul_reward_claimed");
                            gameContext.applyQuestReward(quest.getReward());
                            return "The world tree breathes in peace once more! Accept the Heart of the Void Singularity, bound and purified!";
                        }
                        return "The leaves of Yggdrasoul shimmer with timeless vitality.";
                    } else if (quest != null) {
                        return "The Chaos Abomination still thrashes at the roots. Steel yourself!";
                    }
                    return "The roots run deep beneath all creation.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, elder);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Exowerin", "Go back to Exowerin", Exowerin::new);
    }

    @Override
    public String getName() {
        return "The Yggdrasoul";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }
}
