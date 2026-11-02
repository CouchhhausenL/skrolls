package main.java.screen.impl;

import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.ChatMessageWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.util.SplashText;

public class CreditsScreen extends Screen {
    ScrollableContainerWidget containerWidget;
    private GameContext context;
    @Override
    protected void init(GameContext context) {
        this.context = context;

        containerWidget = new ScrollableContainerWidget(0,0,getWidth(),getHeight(),0);
        addChild(containerWidget);
        for(String line : SplashText.getCredits()){
            containerWidget.addChild(new ChatMessageWidget(0,0,getWidth(),line));
        }
    }

    @Override
    public void onResize() {

    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
    if(event.isEscape()){
        context.setScreen(new MainGameScreen());
    }
    }
}
