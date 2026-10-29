package main.java.screen.impl;


import main.java.PlayerLevel;
import main.java.entity.player.Player;
import main.java.game.GameContext;
import main.java.item.Inventory;
import main.java.item.Item;
import main.java.item.ItemType;
import main.java.scene.scenes.WinterfortCastle;
import main.java.screen.Screen;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

// Opening dialog: welcome message, name input and a choice between the
// tutorial or starting directly in Winterfort Castle.
public class IntroductionScreen extends Screen {


    private DialogWidget dialogWidget;

    @Override
    protected void init(GameContext context) {
        dialogWidget = new DialogWidget(0, 0, 80, 30);
        dialogWidget.addDialogItem(new DialogItem("Hello Dear Traveler! \n" +
                "Thank you for playing this Game,\nit was a hell amount of Work so please sit back and enjoy this:\n\nTEXT ADVENTURE!!",
                AnswerType.NONE,
                () -> {
                }));
        dialogWidget.addDialogItem(new DialogItem("But excuse me, what is your name?",
                AnswerType.INPUT,
                () -> context.setPlayer(new Player(
                        dialogWidget.getEnteredAnswer(),
                        PlayerLevel.NOVICE,
                        new Inventory()
                ))));
        dialogWidget.addDialogItem(new DialogItem(() -> "Hello " + context.getPlayerName() + " nice to Meet you!\n", AnswerType.NONE, () -> {
        }));


        dialogWidget.addDialogItem(new DialogItem("Do you need an Introduction?", AnswerType.BOOLEAN, () -> {
        }));

        dialogWidget.setOnComplete(() -> {


            // Boolean answer selects tutorial vs. immediate start
            if (dialogWidget.getEnteredBoolean()) {
                context.setScreen(new TutorialScene());
            } else {
                // Give the player a basic weapon to start with
                Item commonStaff = ItemType.COMMON_STAFF.create();
                Item commonArmour = ItemType.COMMON_ARMOUR.create();
                Item commonHealthAmulett = ItemType.COMMON_HEALTH_AMULET.create();
                Item commonScroll = ItemType.SCROLL_HARD_SHOVE.create();
                Item healScroll = ItemType.SCROLL_IRON_FORTRESS.create();
                Item teleportScroll = ItemType.SCROLL_TELEPORT.create();
                Item potion = ItemType.COMMON_HEALTH_POTION.create();
                context.addItem(commonArmour);
                context.addItem(commonHealthAmulett);
                context.addItem(commonScroll);
                context.addItem(potion);
                context.addItem(commonStaff);
                context.addItem(healScroll);
                context.addItem(teleportScroll);
                context.equipItem(commonArmour);
                context.equipItem(commonHealthAmulett);
                context.equipItem(commonScroll);
                context.equipItem(commonStaff);
                context.equipItem(healScroll);
                context.equipItem(teleportScroll);

                context.setScreen(new MainGameScreen());
            }

        });

        addChild(dialogWidget);
    }


    @Override
    public void onResize() {

    }
}
