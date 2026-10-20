package main.java.screen.impl;


import main.java.entity.player.Player;
import main.java.game.GameContext;
import main.java.item.Inventory;
import main.java.item.ItemType;
import main.java.scene.scenes.WinterfortCastle;
import main.java.screen.Screen;
import main.java.screen.widget.impl.DialogWidget;

// Opening dialog: welcome message, name input and a choice between the
// tutorial or starting directly in Winterfort Castle.
public class IntroductionScreen extends Screen {


    private DialogWidget dialogWidget;

    @Override
    protected void init(GameContext context) {
        dialogWidget = new DialogWidget(0, 0, 80, 30);
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("Hello Dear Traveler! \n" +
                "Thank you for playing this Game,\nit was a hell amount of Work so please sit back and enjoy this:\n\nTEXT ADVENTURE!!",
                DialogWidget.AnswerType.NONE,
                () -> {
                }));
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("But excuse me, what is your name?",
                DialogWidget.AnswerType.INPUT,
                () -> context.setPlayer(new Player(
                        dialogWidget.getEnteredAnswer(),
                        50,
                        30,
                        new Inventory()
                ))));
        dialogWidget.addDialogItem(new DialogWidget.DialogItem(() -> "Hello " + context.getPlayerName() + " nice to Meet you!\n", DialogWidget.AnswerType.NONE, () -> {
        }));


        dialogWidget.addDialogItem(new DialogWidget.DialogItem("Do you need an Introduction?", DialogWidget.AnswerType.BOOLEAN, () -> {
        }));

        dialogWidget.setOnComplete(() -> {


            // Boolean answer selects tutorial vs. immediate start
            if (dialogWidget.getEnteredBoolean()) {
                context.setScreen(new TutorialScene());
            } else {
                context.setScene(new WinterfortCastle(context));
                // Give the player a basic weapon to start with
                context.addItem(ItemType.COMMON_STAFF.create());
                context.setScreen(new MainGameScreen());
            }

        });

        addChild(dialogWidget);
    }


    @Override
    public void onResize() {

    }
}
