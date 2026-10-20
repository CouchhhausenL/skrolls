package main.java.screen.impl;


import main.java.animation.Animation;
import main.java.battle.BattleContext;
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


    private ScrollableContainerWidget messagesHistoryWidget;

    @Override
    protected void init(GameContext context) {
        BattleContext battle = context.getCurrentBattle();
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
            attackOptions.put(spell.getName(), () -> spell.cast(context));
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

        // Show the main menu first
        switchMenu(optionsWidget);
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

    public void playerAttack(String attackAnimationName) {
        Animation playerAttackAnimation = SkaParser.parse("character/wizard/" + attackAnimationName);
        playerAnimationWidget = new AnimationWidget(0, 0, playerAttackAnimation);
    }

    public void enemyAttack(String monster) {
        Animation enemyAttackAnimation = SkaParser.parse("character/" + monster + "/attack.ska");
        enemyAnimationWidget = new AnimationWidget(0, 0, enemyAttackAnimation);
    }

    @Override
    public void onResize() {

    }
}
