package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.container.BasicContainerWidget;
import main.java.screen.widget.impl.container.KeyValueWidget;
import main.java.screen.widget.impl.container.VerticalContainerWidget;
import main.java.util.Color;
import main.java.util.SkaParser;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fastfetch extends Screen {
    private VerticalContainerWidget container;
    private Map<LabelWidget, LabelWidget> modules;
    private GameContext context;
    @Override
    protected void init(GameContext context) {
        this.context = context;
        Animation logo = SkaParser.parse("logo.ska");
        addChild(new AnimationWidget(0,0,logo));

        container = new VerticalContainerWidget(40,0,80,30,0);
        String topLine = context.getPlayerName().toLowerCase() + "@skrolls";
        container.addChild(new LabelWidget(0,0,topLine));
        container.addChild(new LabelWidget(0,0,"-".repeat(topLine.length())));

        modules = new LinkedHashMap<>();
        addModule("OS: ", "Skrolls");
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        addModule("Uptime: ", getPrettyUptime(uptimeMillis));
        String staff = "None";
        if(context.getInventory().getStaff() != null){
            staff = context.getInventory().getStaff().getName();
        }
        addModule("Staff: ", staff);
        String armor = "None";
        if(context.getInventory().getArmor() != null){
            armor = context.getInventory().getArmor().getName();
        }
        addModule("Backpack Items: ", String.valueOf(context.getInventory().getBackpackItems().size()));
        addModule("Location: ", context.getSceneName() + " ("+context.getSceneDifficulty().toString()+")");
        addModule("Magic Shards: ", String.valueOf(context.getMagicShards()));

        addModule("Player Level: ", context.getPlayerLevel().toString());
        addModule("Max Difficulty Level: ", context.getPlayerDifficultyLevel().toString());
        addModule("Attack: ", String.valueOf(context.getAttackValue()));
        addModule("Defense: ", String.valueOf(context.getDefenseStat()));
        addModule("Speed: ", String.valueOf(context.getPlayerSpeed()));
        addModule("Max Health: ", String.valueOf(context.getPlayerMaxHealth()));
        addModule("Max Mana: ", String.valueOf(context.getPlayerMaxMana()));

        addChild(container);
    }


    private void addModule(String keyString, String entryString ){
        LabelWidget key = new LabelWidget(keyString, Color.GREEN);
        LabelWidget entry = new LabelWidget(entryString, Color.WHITE);
        BasicContainerWidget module = new KeyValueWidget(0,0,key, entry);
        container.addChild(module);
    }

    private String getPrettyUptime(long uptimeMillis){
        int seconds = (int) Math.floor((double) uptimeMillis / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        String string = "";
        if(hours > 0){
            if(hours == 1){
                string += hours + " hour";
            }else {
                string += hours + " hours";
            }
        }
        if(minutes > 0){
            if(minutes == 1){
                string += " "+ minutes + " minute";
            }else {
                string += " "+ (minutes % 60) + " minutes";
            }
        }
        if(seconds == 1){
            string += " " + seconds + " second";
        }else {
            string += " " + (seconds % 60) + " seconds";
        }
        return string;
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
