package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.util.Color;

public class BattleStatusWidget extends Widget {
    private final String name;
    private int health;
    private final int maxHealth;

    public BattleStatusWidget(int x, int y, String name, int health, int maxHealth) {
        super(x, y, 22, 4);
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    @Override
    protected void onDraw(DrawContextView context) {
        int w = getWidth();
        int h = getHeight();


        String border = "+" + "-".repeat(w - 2) + "+";
        context.drawString(0, 0, border, Color.WHITE);
        for (int y = 1; y < h - 1; y++) {
            context.drawString(0, y, "|", Color.WHITE);
            context.drawString(w - 1, y, "|", Color.WHITE);
        }
        context.drawString(0, h - 1, border, Color.WHITE);


        context.drawString(1, 1, name, Color.WHITE);


        context.drawString(1, 2, calculateHealthBar(), Color.GREEN);
    }

    private String calculateHealthBar() {
        int barWidth = 20;
        int filled = maxHealth > 0 ? (health * barWidth) / maxHealth : 0;
        filled = Math.clamp(filled, 0, barWidth);
        int unfilled = barWidth - filled;
        return "■".repeat(filled) + "□".repeat(unfilled);
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
    }


    public void setHealth(int health) {
        this.health = health;
    }
}