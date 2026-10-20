package main.java.screen.impl;


import main.java.battle.Battle;
import main.java.entity.monster.MonsterType;
import main.java.entity.player.Player;
import main.java.game.GameContext;
import main.java.item.Inventory;
import main.java.item.Item;
import main.java.item.ItemType;
import main.java.screen.Screen;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.TextFieldWidget;

public class TutorialScene extends Screen {


    private DialogWidget dialogWidget;

    @Override
    protected void init(GameContext context) {
        dialogWidget = new DialogWidget(0,0, 80, 30);
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("This is your Tutorial \n" +
                "It will explain the game Step by Step!",
                DialogWidget.AnswerType.NONE,
                () -> {}));
        dialogWidget.addDialogItem(new DialogWidget.DialogItem( "First lets give you some Items! \n " +
                "Here I have an Old Wooden Cane laying around, I don't need it anymore.\n" +
                "Do you want to equip it?",
                 DialogWidget.AnswerType.BOOLEAN,
                () -> {
            if(dialogWidget.getEnteredBoolean()){

            Item commonStaff = ItemType.COMMON_STAFF.create();
            context.addItem(commonStaff);
            context.equipItem(commonStaff);
                }else {
            dialogWidget.goToIndex(1);
        }
        }
                ));
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("I also have an Old Armour and an Old Amulette for you", DialogWidget.AnswerType.NONE, () -> {
            Item commonArmour = ItemType.COMMON_ARMOUR.create();
            Item commonHealthAmulett =  ItemType.COMMON_HEALTH_AMULET.create();
            context.addItem(commonArmour);
            context.addItem(commonHealthAmulett);
            context.equipItem(commonArmour);
            context.equipItem(commonHealthAmulett);

        }));
        dialogWidget.addDialogItem(new DialogWidget.DialogItem("Lets try fighting your first Battle", DialogWidget.AnswerType.NONE, () -> {
            context.setBattle(MonsterType.ORK.create());
        }));



        dialogWidget.setOnComplete(() -> {
            context.setScreen(new BattleScreen());



        });
        addChild(dialogWidget);







        }








    @Override
    public void onResize() {

    }
}
