package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.util.Color;

// Fixed-size bordered box showing a combatant's name and a 20-cell health bar
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


        context.drawString(1, 2, calculateStatBar(health, maxHealth, 20), Color.GREEN);
    }

    // Filled cells proportional to current/max health, clamped to the bar width
    public static String calculateStatBar(int stat, int maxStat, int barWidth) {

        int clampedStat = Math.clamp(stat, 0, Math.max(maxStat, 0));

        int filled = 0;
        if (maxStat > 0) {
            filled = Math.round((float) clampedStat / maxStat * barWidth);

            if (clampedStat > 0 && filled == 0) {
                filled = 1;                       // alive -> atleast 1 bar
            }
            if (clampedStat < maxStat && filled == barWidth) {
                filled = barWidth - 1;            //not full hp -> no full health bar
            }
        }

        filled = Math.clamp(filled, 0, barWidth);
        int unfilled = barWidth - filled;
        return "■".repeat(filled) + "□".repeat(unfilled);
    }

    public void update(int health){
        this.health = health;
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
