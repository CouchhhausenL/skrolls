package main.java.screen.impl;


import main.java.animation.Animation;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.BattleStatusWidget;
import main.java.util.SkaParser;

public class BattleScreen extends Screen {
    private AnimationWidget playerAnimationWidget;
    private AnimationWidget enemyAnimationWidget;

    private BattleStatusWidget playerBox;
    private BattleStatusWidget enemyBox;

    @Override
    protected void init(GameContext context) {
        BattleContext battle = context.getCurrentBattle();
        playerBox = new BattleStatusWidget(1,1, battle.getPlayerName(), battle.getPlayerHealth(), battle.getPlayerMaxHealth());
        enemyBox = new BattleStatusWidget(67,  25, battle.getEnemyName(), battle.getEnemyHealth(), battle.getEnemyMaxHealth());


        Animation playerIdleAnimation = SkaParser.parse("character/wizard/idle.ska");
        Animation enemyIdleAnimation = SkaParser.parse("character/" + battle.getEnemyName().toLowerCase().replace(" ","_") + "/idle.ska");


        playerAnimationWidget = new AnimationWidget(0, 0, playerIdleAnimation);
        playerAnimationWidget.setLooping(true);
        enemyAnimationWidget = new AnimationWidget(0 , 0, enemyIdleAnimation);
        enemyAnimationWidget.setLooping(true);

        addChild(enemyAnimationWidget);
        addChild(playerAnimationWidget);
        addChild(playerBox);
        addChild(enemyBox);
    }


    public void playerAttack(String attackAnimationName){
        Animation playerAttackAnimation = SkaParser.parse("character/wizard/" + attackAnimationName);
        playerAnimationWidget = new AnimationWidget(0,0,playerAttackAnimation);
    }

    public void enemyAttack(String monster){
        Animation enemyAttackAnimation = SkaParser.parse("character/" + monster + "/attack.ska");
        enemyAnimationWidget = new AnimationWidget(0,0, enemyAttackAnimation);
    }

    @Override
    public void onResize() {

    }
}
