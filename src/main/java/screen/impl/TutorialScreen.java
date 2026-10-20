package main.java.screen.impl;


import com.sun.source.tree.LambdaExpressionTree;
import main.java.MagicType;
import main.java.animation.Animation;
import main.java.battle.BattleContext;
import main.java.entity.player.Player;
import main.java.game.GameContext;
import main.java.item.Inventory;
import main.java.screen.Screen;
import main.java.screen.widget.impl.*;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.screen.widget.impl.container.VerticalContainerWidget;
import main.java.util.Color;
import main.java.util.SkaParser;
import org.jline.utils.Colors;

import java.util.Map;

public class TutorialScreen extends Screen {


    private VerticalContainerWidget messagesHistoryWidget;

    @Override
    protected void init(GameContext context) {
        LabelWidget welcomeMessage = new LabelWidget(0,0,"Hey you!");






        welcomeMessage.setFancyMode(true);
        LabelWidget question = new LabelWidget(0,0, "What is your name");
        question.setFancyMode(true);
        TextFieldWidget nameField = new TextFieldWidget(0,0,40, 1, TextFieldWidget.DEFAULT_COLORS);
        nameField.setPlaceholder("...");

        messagesHistoryWidget = new VerticalContainerWidget(0,0,80,20,1);
        messagesHistoryWidget.addChild(welcomeMessage);
        messagesHistoryWidget.addChild(question);
        messagesHistoryWidget.addChild(nameField);

        addChild(messagesHistoryWidget);

        LabelWidget continueHint = new LabelWidget(0, messagesHistoryWidget.getHeight(),"Press [ENTER] to continue");
        addChild(continueHint);

        OptionsWidget optionsWidget = new OptionsWidget(0,30, 20, 10, Color.WHITE, Map.of(
                "Create Player", () -> {context.setPlayer(new Player(nameField.getContent(), 10, 10,new Inventory()));}
        ));

        addChild(optionsWidget);

        }








    @Override
    public void onResize() {

    }
}
