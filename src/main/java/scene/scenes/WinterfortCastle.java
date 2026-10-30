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
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class WinterfortCastle extends LocationScene {
    private Chest chest;
    private NPC queen;

    public WinterfortCastle(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("winterfort_castle_chest_1","Chest", Quality.COMMON);
        queen = new NPC("winterfort_castle_queen","Queen of Winterfort", NPCType.QUEEN, this);
        queen.addDialogItem(new DialogItem("Hello, dear aspiring wizard's apprentice"));
        queen.addDialogItem(new DialogItem("The Kingdom of Winterfort needs your help!\nOur territories are being overrun by monsters and we can't defend ourselves."));
        queen.addDialogItem(new DialogItem("Can you please kill 5 Axe Rogues for me?", AnswerType.BOOLEAN, ()->{
            if(queen.getEnteredBoolean()) {
                gameContext.addQuest(new KillQuest("winterfort_castle_queen_quest_kill_axe_rogues",
                        "Kill 5 Axe Rogues", 5, new QuestReward(
                        30, ItemType.COMMON_WARRIOR_RING.create()),
                        queen,
                        MonsterType.AXE_ROGUE
                ));
            }else{
                gameContext.markUnInteracted("winterfort_castle_queen");
            }

        }));
        queen.addDialogItem(new DialogItem(
                () -> queen.getEnteredBoolean()
                        ? "Thank you for your Loyality to the Kingdom!\nOnce you finish please come back to me, so I can reward you handsomely!"
                        : "You dare to defy the queen's wish?\nThan I can't help you either - may shame fall upon you.",
                AnswerType.NONE,
                ()->{})
        );
        queen.addInteractedDialog(new DialogItem(
                () -> {
                    Quest quest = gameContext.getQuest("winterfort_castle_queen_quest_kill_axe_rogues");
                    if(quest.isComplete()){
                        gameContext.applyQuestReward(quest.getReward());
                        return "Thank you for your help dear aspiring Wizard";
                    }
                    else{
                        return "You have only killed "+ quest.getProgress()+" out of "+quest.getRequired()+" Axe Rogues. Only dare to come to come back to me if you're finished!";
                    }

                    },


                AnswerType.NONE,
                ()->{}
        ));

    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, queen);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Winterfort", "Leave Winterfort Castle", Winterfort::new);
    }

    @Override
    public String getName() {
        return "Winterforst Castle";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.NONE;
    }

}
