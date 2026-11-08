package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.game.GameContext;
import main.java.scene.scenes.WinterfortCastle;
import main.java.screen.DrawContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.ChatMessageWidget;
import main.java.screen.widget.impl.VerbInputWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.util.SkaParser;

// Main exploration screen: scrollable message log on top, verb input at the bottom
public class MainGameScreen extends Screen {
    private static final int VERB_INPUT_HEIGHT = 6;

    // Cached message log hash to detect changes without rebuilding every frame
    private int lastMessagesCode = 0;
    private GameContext context;

    private VerbInputWidget verbInputWidget;
    private ScrollableContainerWidget messageContainerWidget;
    private AnimationWidget sceneAnimation;

    @Override
    protected void init(GameContext context) {
        if(context.getSceneName() == null){
            context.setScene(new WinterfortCastle(context));
        }
        setSceneAnimation(context.getSceneName());
        this.context = context;

        verbInputWidget = new VerbInputWidget(context, 0, 0, 1, 1);
        messageContainerWidget = new ScrollableContainerWidget(0, 0, 1, 1, 2);
        messageContainerWidget.setOnlyScrollOnOverflow(true);
        messageContainerWidget.setKeyboardInput(false);
        messageContainerWidget.setAutoScroll(true);
        positionWidgets();
        addChild(sceneAnimation);
        addChild(messageContainerWidget);
        addChild(verbInputWidget);
    }

    // Input bar is pinned to the bottom; the log fills the remaining space
    private void positionWidgets() {

        verbInputWidget.setPosition(0, getHeight() - VERB_INPUT_HEIGHT);
        verbInputWidget.setDimensions(getWidth(), VERB_INPUT_HEIGHT);

        messageContainerWidget.setPosition(0, 30);
        messageContainerWidget.setDimensions(getWidth(), Math.max(1, getHeight() - VERB_INPUT_HEIGHT - 30));

        updateMessages();
    }

    // Rebuilds the log widgets from the current message list
    private void updateMessages() {
        messageContainerWidget.clearChildren();
        for (String message : context.getMessages()) {
            messageContainerWidget.addChild(new ChatMessageWidget(
                    0, 0, getWidth() / 2, message
            ));
        }
        messageContainerWidget.scrollToBottom();
    }

    @Override
    public void onResize() {
        positionWidgets();
    }


    public void setSceneAnimation(String sceneName) {
        String pathName = sceneName.toLowerCase().replace(" ", "_") + ".ska";
        Animation animation = SkaParser.parse("scene/" + pathName);
        if (sceneAnimation == null) {
            sceneAnimation = new AnimationWidget(0, 0, animation);
        } else {
            sceneAnimation.setAnimation(animation);
        }
    }

    // Only rebuild the log when the message list actually changed
    @Override
    protected void onPreDraw(DrawContext context) {
        int newHashCode = this.context.getMessagesHashCode();
        if (lastMessagesCode != newHashCode) {
            lastMessagesCode = newHashCode;
            updateMessages();
        }
    }
}
