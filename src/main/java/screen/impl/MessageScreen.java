package main.java.screen.impl;


import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;
import main.java.util.Color;

public class MessageScreen extends Screen {


    private DialogWidget messageWidget;
    private String message = "Here should stand a message!";


    @Override
    protected void init(GameContext context) {
        messageWidget = new DialogWidget(getWidth()/2 - LabelWidget.calculateWidth(message)/2, getHeight()/2, getWidth(), getHeight());
        messageWidget.addDialogItem(new DialogItem(message, AnswerType.NONE, () -> {}));

        addChild(messageWidget);
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setOnComplete(Runnable onComplete){
        messageWidget.setOnComplete(onComplete);
    }


    @Override
    public void onResize() {
        messageWidget.setPosition(getWidth() / 2 - LabelWidget.calculateWidth(message) / 2, getHeight()/2);

    }
}
