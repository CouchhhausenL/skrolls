package main.java.screen.impl;


import main.java.animation.Animation;
import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.battle.BattleState;
import main.java.console.input.KeyInputEvent;
import main.java.entity.monster.Monster;
import main.java.game.GameContext;
import main.java.item.Item;
import main.java.item.ItemHelper;
import main.java.item.items.Potion;
import main.java.screen.Screen;
import main.java.screen.widget.Widget;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.BattleStatusWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.screen.widget.impl.options.ItemOption;
import main.java.screen.widget.impl.options.Option;
import main.java.spell.Spell;
import main.java.util.Color;
import main.java.util.SkaParser;
import main.java.util.SplashText;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Battle view: status boxes, idle animations and three switchable menus
// (main actions, attack spells, potions) plus the scrolling battle log.
public class BattleScreen extends Screen {
    private AnimationWidget playerAnimationWidget;
    private AnimationWidget enemyAnimationWidget;

    private BattleStatusWidget playerBox;
    private BattleStatusWidget enemyBox;
    private OptionsWidget optionsWidget;
    private OptionsWidget attackOptionsWidget;
    private OptionsWidget potionOptionsWidget;

    private Battle.BattleStateOrder battleStateOrder;

    private GameContext context;
    private BattleContext battle;




    private ScrollableContainerWidget messagesHistoryWidget;

    @Override
    protected void init(GameContext context) {
        this.context = context;
        this.battle = context.getCurrentBattle();
        battle.fightInit();
        playerBox = new BattleStatusWidget(1, 1, battle.getPlayerName(), battle.getPlayerHealth(), battle.getPlayerMaxHealth());
        enemyBox = new BattleStatusWidget(67, 25, battle.getEnemyName(), battle.getEnemyHealth(), battle.getEnemyMaxHealth());


        // Enemy assets are named after the monster ("Axe Rogue" -> "axe_rogue")
        Animation playerIdleAnimation = SkaParser.parse("character/wizard/idle.ska");
        Animation enemyIdleAnimation = SkaParser.parse("character/" + battle.getEnemyName().toLowerCase().replace(" ", "_") + "/idle.ska");


        playerAnimationWidget = new AnimationWidget(0, 0, playerIdleAnimation);
        playerAnimationWidget.setLooping(true);
        enemyAnimationWidget = new AnimationWidget(0, 0, enemyIdleAnimation);
        enemyAnimationWidget.setLooping(true);

        // 1. Main menu
        optionsWidget = new OptionsWidget(0, 30, 40, 10);
        optionsWidget.addOption(new Option("Attack", () -> switchMenu(attackOptionsWidget)));
        optionsWidget.addOption(new Option("Drink Potion", () -> switchMenu(potionOptionsWidget)));
        optionsWidget.addOption(new Option("Flee", context::fleeFromBattle));

        // 2. Attack menu: one entry per spell from equipped scrolls
        attackOptionsWidget = new OptionsWidget(0, 30, 40, 10);
        for(Spell spell : context.getSpells()){
            attackOptionsWidget.addOption(new Option(spell.getName(), () -> {playerAttack(context, spell);}));
        }
        attackOptionsWidget.addOption(new Option("Back", () -> switchMenu(optionsWidget)));

        // 3. Potion menu
        potionOptionsWidget = new OptionsWidget(0, 30, 40, 10);
        for (Potion potion : context.getPotions()) {
            potionOptionsWidget.addOption(new ItemOption(potion.getName(), ItemHelper.getItemColor(potion), potion, () -> {playerDrinkPotion(context, potion);}));
        }
        potionOptionsWidget.addOption(new Option("Back", () -> switchMenu(optionsWidget)));

        // 4. Battle messages
        messagesHistoryWidget = new ScrollableContainerWidget(0, 40, 40, 10, 0);
        updateMessagesWidget();
        messagesHistoryWidget.setKeyboardInput(false);
        addChild(messagesHistoryWidget);

        // Register the base widgets
        addChild(enemyAnimationWidget);
        addChild(playerAnimationWidget);
        addChild(playerBox);
        addChild(enemyBox);

        battleStateOrder = battle.determineBattleStateOrder();

        setState(battleStateOrder.first());
    }

    // Removes all option widgets and shows only the requested one
    public void switchMenu(OptionsWidget newMenu) {
        removeChild(optionsWidget);
        removeChild(attackOptionsWidget);
        removeChild(potionOptionsWidget);

        if (newMenu != null) {
            addChild(newMenu);
        }
    }

    public void checkEnd(){
        if(!battle.isEnemyAlive()){

            AnimationWidget winAnimation = new AnimationWidget(0,0, 80, 30, SkaParser.parse("utility/win.ska"));
            winAnimation.onDone(() -> {
                Item item = battle.MobDrop();
                context.addMessage(battle.getEnemyName() + " dropped "+ item.getName() + "! You stashed it in your backpack.");
               context.setScreen(new MainGameScreen());
            });
            addChild(winAnimation);

        }else if(!battle.isPlayerAlive()){
            AnimationWidget looseAnimation = new AnimationWidget(0,0,80,30, SkaParser.parse(("utility/loose.ska")));
            looseAnimation.onDone((() -> {new MainGameScreen();

                context.addMessage(SplashText.getDeath());

            }));

        }
    }


    public void updateMessagesWidget(){
        List<String> battleLog = battle.getBattleLog();
        messagesHistoryWidget.clearChildren();
        for (String message : battleLog) {
            LabelWidget widget = new LabelWidget(0, 0, message);
            messagesHistoryWidget.addChild(widget);
        }
    }

    public void switchState(){
        updateMessagesWidget();
        BattleState currentBattleState = getCurrentState(battle);
        if(currentBattleState == battleStateOrder.first()){
            battle.setBattleState(battleStateOrder.second());
        } else if (currentBattleState == battleStateOrder.second()){
            battle.setBattleState(BattleState.END_ROUND);
        }

        setState(battle.getBattleState());
    }

    public void setState(BattleState battleState){
        if (!battle.isEnemyAlive()) {
            battle.sendMessage(battle.getEnemyName() + " was defeated!");
            updateMessagesWidget();
            switchMenu(null);
            return;
        }
        if (!battle.isPlayerAlive()) {
            battle.sendMessage("You were defeated!");
            updateMessagesWidget();
            switchMenu(null);
            return;
        }

        switch(battleState){
            case PLAYER_TURN -> switchMenu(optionsWidget);
            case ENEMY_TURN -> {
                switchMenu(null);
                enemyAttack(battle);
            }
            case END_ROUND -> {
                switchMenu(null);
                battle.endRound();
                playerBox.update(battle.getPlayerHealth());
                enemyBox.update(battle.getEnemyHealth());
                if (!battle.isEnemyAlive() || !battle.isPlayerAlive()) {
                    setState(BattleState.END_ROUND);
                } else {
                    battleStateOrder = battle.determineBattleStateOrder();
                    setState(battleStateOrder.first());
                }
            }
        }
    }

    public BattleState getCurrentState(BattleContext battle){
        return battle.getBattleState();
    }




    public void playerAttack(GameContext context, Spell spell) {
        BattleContext battle = context.getCurrentBattle();

        switchMenu(null);

        removeChild(playerAnimationWidget); //remove old widget
        Animation playerAttackAnimation = SkaParser.parse("character/wizard/" + spell.getAnimationName());

        AnimationWidget attackWidget  = new AnimationWidget(0, 0, playerAttackAnimation);
        attackWidget.onDone(() -> {
            spell.cast(context);
            enemyBox.update(battle.getEnemyHealth());
            removeChild(attackWidget);
            addChild(playerAnimationWidget);
            checkEnd();
            switchState();
        });
        addChild(attackWidget);
    }




    public void playerDrinkPotion(GameContext context, Potion potion) {
        BattleContext battle = context.getCurrentBattle();

        switchMenu(null);
        potionOptionsWidget.removeByItem(potion);

        removeChild(playerAnimationWidget); //remove old widget
        Animation playerAttackAnimation = SkaParser.parse("character/wizard/drink_potion.ska");

        AnimationWidget drinkWidget  = new AnimationWidget(0, 0, playerAttackAnimation);
        drinkWidget.onDone(() -> {
            battle.drinkPotion(potion);

            enemyBox.update(battle.getEnemyHealth());
            playerBox.update(battle.getPlayerHealth());
            removeChild(drinkWidget);
            addChild(playerAnimationWidget);

                battle.sendMessage("You drank the potion: " + potion.getName());
                checkEnd();
            switchState();
        });
        addChild(drinkWidget);
    }


    public void enemyAttack(BattleContext battle) {
        String monster = battle.getEnemyName().toLowerCase().replace(" ", "_");
        removeChild(enemyAnimationWidget);


        Animation enemyAttackAnimation = SkaParser.parse("character/" + monster + "/attack.ska");
        AnimationWidget enemyAttackWidget = new AnimationWidget(0, 0, enemyAttackAnimation);
        enemyAttackWidget.onDone( () -> {
            battle.monsterAttack();
            playerBox.update(battle.getPlayerHealth());
            removeChild(enemyAttackWidget);
            addChild(enemyAnimationWidget);
           checkEnd();
            switchState();
        });
        addChild(enemyAttackWidget);
    }

    @Override
    public void onResize() {

    }


}
