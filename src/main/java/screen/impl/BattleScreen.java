package main.java.screen.impl;


import main.java.animation.Animation;
import main.java.battle.Battle;
import main.java.battle.BattleContext;
import main.java.battle.BattleState;
import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.item.items.Potion;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.BattleStatusWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.spell.Spell;
import main.java.util.Color;
import main.java.util.SkaParser;

import java.util.LinkedHashMap;
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




    private ScrollableContainerWidget messagesHistoryWidget;

    @Override
    protected void init(GameContext context) {
        BattleContext battle = context.getCurrentBattle();
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
        Map<String, Runnable> mainOptions = new LinkedHashMap<>();
        mainOptions.put("Attack", () -> switchMenu(attackOptionsWidget));
        mainOptions.put("Drink Potion", () -> switchMenu(potionOptionsWidget));
        mainOptions.put("Flee", context::fleeFromBattle);
        optionsWidget = new OptionsWidget(0, 40, 40, 20, Color.WHITE, mainOptions);

        // 2. Attack menu: one entry per spell from equipped scrolls
        Map<String, Runnable> attackOptions = new LinkedHashMap<>();
        for (Spell spell : context.getSpells()) {
            attackOptions.put(spell.getName(), () -> {
                playerAttack(context, spell);
            });
        }
        attackOptions.put("Back", () -> switchMenu(optionsWidget));
        attackOptionsWidget = new OptionsWidget(0, 40, 40, 20, Color.WHITE, attackOptions);

        // 3. Potion menu
        Map<String, Runnable> potionOptions = new LinkedHashMap<>();
        for (Potion potion : context.getPotions()) {
            potionOptions.put(potion.getName(), () -> context.getCurrentBattle().drinkPotion(potion));
        }
        potionOptions.put("Back", () -> switchMenu(optionsWidget));
        potionOptionsWidget = new OptionsWidget(0, 40, 40, 20, Color.WHITE, potionOptions);

        // 4. Battle messages
        messagesHistoryWidget = new ScrollableContainerWidget(0, 30, 40, 10, 0);
        for (String message : battle.getBattleLog()) {
            LabelWidget widget = new LabelWidget(0, 0, message);
            messagesHistoryWidget.addChild(widget);
        }
        addChild(messagesHistoryWidget);

        // Register the base widgets
        addChild(enemyAnimationWidget);
        addChild(playerAnimationWidget);
        addChild(playerBox);
        addChild(enemyBox);

        battleStateOrder = battle.determineBattleStateOrder();

        setState(battle, battleStateOrder.first());
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


    public void switchState(BattleContext battle){
        BattleState currentBattleState = getCurrentState(battle);
        if(currentBattleState == battleStateOrder.first()){
            battle.setBattleState(battleStateOrder.second());
        } else if (currentBattleState == battleStateOrder.second()){
            battle.setBattleState(BattleState.END_ROUND);
        }

        setState(battle, battle.getBattleState());
    }

    public void setState(BattleContext battle, BattleState battleState){
        if (!battle.isEnemyAlive()) {
            battle.sendMessage(battle.getEnemyName() + " was defeated!");
            switchMenu(null);
            return;
        }
        if (!battle.isPlayerAlive()) {
            battle.sendMessage("You were defeated!");
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
                    setState(battle, BattleState.END_ROUND);
                } else {
                    battleStateOrder = battle.determineBattleStateOrder();
                    setState(battle, battleStateOrder.first());
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
            if (!battle.isEnemyAlive()) {
                battle.sendMessage(battle.getEnemyName() + " was defeated!");
                return;
            }
            switchState(battle);
        });
        addChild(attackWidget);
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
            if (!battle.isPlayerAlive()) {
                battle.sendMessage("You were defeated!");
                return;
            }
            switchState(battle);
        });
        addChild(enemyAttackWidget);
    }

    @Override
    public void onResize() {

    }


}
