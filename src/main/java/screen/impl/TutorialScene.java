package main.java.screen.impl;


import main.java.entity.monster.MonsterType;
import main.java.game.GameContext;
import main.java.item.Item;
import main.java.item.ItemType;
import main.java.screen.Screen;
import main.java.screen.widget.impl.DialogWidget;

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
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("This is your Tutorial \n" +
                "It will explain the game Step by Step!",
                DialogWidget.AnswerType.NONE,
                () -> {
                }));
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("First lets give you some Items! \n " +
                "Here I have an Old Wooden Cane laying around, I don't need it anymore.\n" +
                "Do you want to equip it?",
                DialogWidget.AnswerType.BOOLEAN,
                () -> {
                    // "No" sends the dialog back so the question repeats
                    if (dialogWidget.getEnteredBoolean()) {

                        Item commonStaff = ItemType.COMMON_STAFF.create();
                        context.addItem(commonStaff);
                        context.equipItem(commonStaff);
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
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("I also have an Old Armour and an Old Amulette for you", DialogWidget.AnswerType.NONE, () -> {
            Item commonArmour = ItemType.COMMON_ARMOUR.create();
            Item commonHealthAmulett = ItemType.COMMON_HEALTH_AMULET.create();
            Item commonScroll = ItemType.SCROLL_HARD_SHOVE.create();
            Item potion = ItemType.COMMON_HEALTH_POTION.create();
            context.addItem(commonArmour);
            context.addItem(commonHealthAmulett);
            context.addItem(commonScroll);
            context.addItem(potion);
            context.equipItem(commonArmour);
            context.equipItem(commonHealthAmulett);
            context.equipItem(commonScroll);


        }));
        // Last step sets up the battle; completing the dialog enters the fight
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("On every Path you can fight Monsters by typing \"fight\"! Lets try fighting your first Battle",
                DialogWidget.AnswerType.NONE, () -> context.setBattle(MonsterType.INFINITY_DRAGON.create())));




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
