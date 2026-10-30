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

public class Ashwick extends SettlementScene {
    private final Chest chest;
    private final NPC salesman;
    private final NPC blacksmith;

    public Ashwick(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("ashwick_chest_1", "Ashen Iron Chest", Quality.RARE);

        salesman = new NPC("ashwick_salesman", "Ashwick Apothecary", NPCType.SALESMAN, this);
        salesman.addDialogItem(new DialogItem("Welcome to Ashwick! The sulfur and soot do wonders for potion brewing."));
        salesman.addDialogItem(new DialogItem("Do you want to buy a flask of Liquid Granite for 40 Magic Crystals?", AnswerType.BOOLEAN, () -> {
            if (salesman.getEnteredBoolean()) {
                if (gameContext.spendMagicShards(40)) {
                    gameContext.addItem(ItemType.RARE_STONEFORM_ELIXIR.create());
                } else {
                    gameContext.markUnInteracted("ashwick_salesman");
                }
            } else {
                gameContext.markUnInteracted("ashwick_salesman");
            }
        }));
        salesman.addDialogItem(new DialogItem(
                () -> {
                    if (!salesman.getEnteredBoolean()) {
                        return "Be careful near the Treacherous Forest then.";
                    }
                    if (!gameContext.hasInteracted("ashwick_salesman")) {
                        return "You don't have enough Magic Crystals! Come back when you have 40.";
                    }
                    return "Drink it before heavy blows land! Safe journeys.";
                },
                AnswerType.NONE,
                () -> {}
        ));
        salesman.addInteractedDialog(new DialogItem("Always good to see seasoned spellcasters in Ashwick."));

        blacksmith = new NPC("ashwick_blacksmith", "Borin the Smith", NPCType.DWARF, this);
        blacksmith.addDialogItem(new DialogItem("Greetings, traveler! Clanging iron is all that keeps the monsters back."));
        blacksmith.addDialogItem(new DialogItem("Vicious Blade Brutes roam the ash fields and dull our blades.\nCan you strike down 3 Blade Brutes for me?", AnswerType.BOOLEAN, () -> {
            if (blacksmith.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest(
                        "ashwick_quest_kill_blade_brutes",
                        "Crush 3 Blade Brutes",
                        3,
                        new QuestReward(60, ItemType.RARE_WAR_PIKE.create()),
                        blacksmith,
                        MonsterType.BLADE_BRUTE
                ));
            } else {
                gameContext.markUnInteracted("ashwick_blacksmith");
            }
        }));
        blacksmith.addDialogItem(new DialogItem(
                () -> blacksmith.getEnteredBoolean() ? "Aye! Hammer them into scrap, wizard!" : "Bah! I'll have to forge thicker armor myself.",
                AnswerType.NONE,
                () -> {}
        ));
        blacksmith.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("ashwick_quest_kill_blade_brutes");
                    if (quest != null && quest.isComplete()) {
                        if (!gameContext.hasInteracted("ashwick_reward_claimed")) {
                            gameContext.markInteracted("ashwick_reward_claimed");
                            gameContext.applyQuestReward(quest.getReward());
                            return "By the hammer! You smashed those brutes! Take this Ironclad War Staff fresh off the anvil!";
                        }
                        return "My forge rings clear thanks to your help!";
                    } else if (quest != null) {
                        return "You've crushed " + quest.getProgress() + " / " + quest.getRequired() + " Blade Brutes so far.";
                    }
                    return "Keep your chin up and your steel sharp.";
                },
                AnswerType.NONE,
                () -> {}
        ));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, salesman, blacksmith);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_EAST, "Highland Boulevard", HighlandBoulevard::new);
        exits.addExit(Direction.SOUTH_WEST, "Silt Passage", SiltPassage::new);
        exits.addExit(Direction.SOUTH_EAST, "Gilded Route", GildedRoute::new);
        exits.addExit("SoulObelisk", "Enter Soul Obelisk", SoulObelisk::new);
        exits.addExit("Treacherous Forest", "Go to surrounding Treacherous Forest", TreacherousForest::new);
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }

    @Override
    public String getName() {
        return "Ashwick";
    }
}
