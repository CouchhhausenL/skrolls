package main.java.screen.impl;


import main.java.entity.monster.MonsterType;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

// Step-by-step tutorial dialog that hands out starter gear and a test battle
public class TutorialScene extends Screen {


    private DialogWidget dialogWidget;
    private int startIndex = 0;

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    protected void init(GameContext context) {
        dialogWidget = new DialogWidget(0, 0, 80, 30);
        dialogWidget.addDialogItem(new DialogItem("This is your Tutorial \n" +
                "It will explain the game Step by Step!",
                AnswerType.NONE,
                () -> {
                }));
        dialogWidget.addDialogItem(new DialogItem("First lets give you some Items! \n " +
                "Here I have an Old Wooden Cane laying around, I don't need it anymore.\n" +
                "Do you want to equip it?",
                AnswerType.BOOLEAN,
                () -> {
                    // "No" sends the dialog back so the question repeats
                    if (dialogWidget.getEnteredBoolean()) {
                    } else {
                        MessageScreen messageScreen = new MessageScreen();
                        messageScreen.setMessage("Good try, but you need to have an Staff! Lets try it again...");
                        context.setScreen(messageScreen);
                        messageScreen.setOnComplete(() -> {
                            TutorialScene tutorialScene = new TutorialScene();
                            tutorialScene.setStartIndex(1);
                            context.setScreen(tutorialScene);
                        });
                    }
                }
        ));
        dialogWidget.addDialogItem(new DialogItem("I'll also hand you some starter equipment\n" +
                "You can always use the inventory (show inventory) or verbs like 'equip' or 'unequip'", AnswerType.NONE, () -> {
            context.initGame();


        }));
        dialogWidget.addDialogItem(new DialogItem("Everything is "));

        // Last step sets up the battle; completing the dialog enters the fight
        dialogWidget.addDialogItem(new DialogItem("On every Path you can fight Monsters by typing \"fight\"! Lets try fighting your first Battle",
                AnswerType.NONE, () -> context.setBattle(MonsterType.AXE_ROGUE.create())));




        // Jump to the requested start index after all items are registered
        if (startIndex > 0) {
            dialogWidget.goToIndex(startIndex);
        }

        addChild(dialogWidget);


    }


    @Override
    public void onResize() {

    }
}
