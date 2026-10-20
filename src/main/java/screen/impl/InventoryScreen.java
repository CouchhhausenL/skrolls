package main.java.screen.impl;


import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.LabelWidget;

public class InventoryScreen extends Screen {

    private GameContext context;

    @Override
    protected void init(GameContext context) {
        this.context = context;

        addChild(new LabelWidget(1, 1, "Very WIP! Exit with escape"));
    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isEscape())
            context.setScreen(new MainGameScreen());
    }

    @Override
    public void onResize() {

    }
}
