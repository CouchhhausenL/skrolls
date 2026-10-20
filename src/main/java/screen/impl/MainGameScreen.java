package main.java.screen.impl;

import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.ChatMessageWidget;
import main.java.screen.widget.impl.VerbInputWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;

public class MainGameScreen extends Screen {
    private static final int VERB_INPUT_HEIGHT = 6;

    private GameContext context;

    private VerbInputWidget verbInputWidget;
    private ScrollableContainerWidget messageContainerWidget;

    @Override
    protected void init(GameContext context) {
        this.context = context;
        verbInputWidget = new VerbInputWidget(context, 0, 0, 1, 1);
        messageContainerWidget = new ScrollableContainerWidget(0, 0, 1, 1, 2);

        positionWidgets();
        addChild(messageContainerWidget);
        addChild(verbInputWidget);
    }

    private void positionWidgets() {
        verbInputWidget.setPosition(0, getHeight() - VERB_INPUT_HEIGHT);
        verbInputWidget.setDimensions(getWidth(), VERB_INPUT_HEIGHT);

        messageContainerWidget.setDimensions(getWidth(), getHeight() - VERB_INPUT_HEIGHT);

        updateMessages();
    }

    private void updateMessages() {
        messageContainerWidget.clearChildren();
        for (String message : context.getMessages()) {
            messageContainerWidget.addChild(new ChatMessageWidget(
                    0, 0, getWidth() / 2, message
            ));
        }
    }

    @Override
    public void onResize() {
        positionWidgets();
    }
}
