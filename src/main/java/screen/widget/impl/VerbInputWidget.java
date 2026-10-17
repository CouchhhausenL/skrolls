package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.game.VerbGameContext;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;

import java.util.ArrayList;
import java.util.List;

public class VerbInputWidget extends Widget {
    private final VerbGameContext context;
    private final String content = "";
    private final List<String> suggestions = new ArrayList<>();

    public VerbInputWidget(VerbGameContext context, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.context = context;
    }

    @Override
    protected void onDraw(DrawContextView context) {

    }

    @Override
    public void onKeyInput(KeyInputEvent event) {

    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
    }
}
