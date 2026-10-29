package main.java.screen.impl;


import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

public class ConfirmationScreen extends MessageScreen {


    private DialogWidget messageWidget;
    private String message = "Here should stand a message!";

    private Runnable onConfirm = ()->{};
    private Runnable onCancel =()->{};


    @Override
    protected void init(GameContext context) {
        messageWidget = new DialogWidget(getWidth()/2 - LabelWidget.calculateWidth(message)/2, getHeight()/2, getWidth(), getHeight());
        messageWidget.addDialogItem(new DialogItem(message, AnswerType.BOOLEAN, () -> {
            if(messageWidget.getEnteredBoolean()){
                onConfirm.run();
            }else{
                onCancel.run();
            }
        }));

        addChild(messageWidget);
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setOnConfirm(Runnable onConfirm){
        this.onConfirm = onConfirm;
    }
    public void setOnCancel(Runnable onCancel){
        this.onCancel = onCancel;
    }


    @Override
    public void onResize() {
        messageWidget.setPosition(getWidth() / 2 - LabelWidget.calculateWidth(message) / 2, getHeight()/2);

    }
}
