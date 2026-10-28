package main.java.screen.impl;

import main.java.PlayerLevel;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.container.KeyValueWidget;
import main.java.screen.widget.impl.container.VerticalContainerWidget;
import main.java.screen.widget.impl.options.Option;
import main.java.util.SplashText;

public class UpgradeScreen extends Screen {
    private VerticalContainerWidget container;

    @Override
    protected void init(GameContext context) {
        container = new VerticalContainerWidget(0,0,50,20,0);

        container.addChild(new KeyValueWidget("Current Mana Shards: ", 0x8eaff0, String.valueOf(context.getMagicShards())));
        PlayerLevel playerLevel = context.getPlayerLevel();
        PlayerLevel nextLevel;
        if(playerLevel.ordinal() >= PlayerLevel.values().length){
                nextLevel = playerLevel;
        }else{
            nextLevel = PlayerLevel.values()[playerLevel.ordinal() + 1];
        }
        int keyColor = 0xf3cf83;

        container.addChild(new KeyValueWidget("Your current Level: ", keyColor, playerLevel.toString()));
        container.addChild(new KeyValueWidget("Next Level: ", keyColor, nextLevel.toString()));
        container.addChild(new LabelWidget("-----------"));
        container.addChild(new LabelWidget("Upgraded Stats: "));
        container.addChild(new KeyValueWidget("Max Health: ", keyColor, formatStatUpgrade(playerLevel.getBaseMaxHealth(), nextLevel.getBaseMaxHealth())));
        container.addChild(new KeyValueWidget("Max Mana: ", keyColor, formatStatUpgrade(playerLevel.getBaseMaxMana(), nextLevel.getBaseMaxMana())));
        container.addChild(new KeyValueWidget("Mana Regen: ", keyColor, formatStatUpgrade(playerLevel.getManaRegen(), nextLevel.getManaRegen())));
        container.addChild(new KeyValueWidget("Attack: ", keyColor, formatStatUpgrade(playerLevel.getBaseAttack(), nextLevel.getBaseAttack())));
        container.addChild(new KeyValueWidget("Defense: ", keyColor, formatStatUpgrade(playerLevel.getBaseDefense(), nextLevel.getBaseDefense())));
        container.addChild(new KeyValueWidget("Speed: ", keyColor, formatStatUpgrade(playerLevel.getBaseSpeed(), nextLevel.getBaseSpeed())));
        container.addChild(new LabelWidget("----------------"));
        container.addChild(new KeyValueWidget("Upgrade Cost: ", 0x8eaff0, String.valueOf(nextLevel.getCost())));
        container.addChild(new LabelWidget(0,0,"Do you want to upgrade?"));
        OptionsWidget optionsWidget = new OptionsWidget(0,0,getWidth(),3);
        optionsWidget.addOption(new Option("YES",() -> {

            MessageScreen messageScreen = new MessageScreen();

            if(context.spendMagicShards(nextLevel.getCost())) {


                messageScreen.setMessage("You successfully ascended from " + playerLevel + " to " + nextLevel);
                context.upgradePlayerLevel();
                context.setScreen(messageScreen);
                messageScreen.setOnComplete(() -> context.setScreen(new MainGameScreen()));
            }else{
                messageScreen.setMessage(SplashText.getNo() +"\n"+"You don't have enough Magic Shards! Go earn some by fighting monsters or sacrificing items!");
                context.setScreen(messageScreen);
                messageScreen.setOnComplete(()->context.setScreen(new UpgradeScreen()));
            }

        }));
        optionsWidget.addOption(new Option("NO", ()->{
            MessageScreen messageScreen = new MessageScreen();
            messageScreen.setMessage("You didn't ascend and are still a "+ playerLevel);
            context.setScreen(messageScreen);
            messageScreen.setOnComplete(()->context.setScreen(new MainGameScreen()));
        }));

        container.addChild(optionsWidget);

        addChild(container);
        autoPositionWidget(container);

    }

    public String formatStatUpgrade(int stat, int upgraded){
        if(stat == upgraded){
            return stat + " (max)";
        }
        return stat + " -> " + upgraded + " (+"+(upgraded-stat) +")";
    }



    @Override
    public void onResize() {

    }
}
