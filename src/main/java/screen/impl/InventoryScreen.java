package main.java.screen.impl;


import main.java.MagicType;
import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.item.Item;
import main.java.item.MagicItem;
import main.java.item.items.*;
import main.java.screen.Screen;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.util.Color;

import java.util.LinkedHashMap;

// Placeholder inventory screen; closing returns to the main game screen
public class InventoryScreen extends Screen {

    private GameContext context;
    private OptionsWidget itemList;
    private LabelWidget detailedName;
    private LabelWidget detailedDescription;

    @Override
    protected void init(GameContext context) {
        this.context = context;

        addChild(new LabelWidget(1, 1, "Very WIP! Exit with escape"));
        itemList = new OptionsWidget(0,0,80,30,  new LinkedHashMap<>(){{
            put(new OptionsWidget.ColoredString("----- Staff -----", Color.WHITE), ()->{});
            put(renderItem(context.getInventory().getStaff()), ()->{});
            put(new OptionsWidget.ColoredString( "----- Armor -----", Color.WHITE), ()->{});

            put(renderItem(context.getInventory().getArmor()), ()->{});
            put(new OptionsWidget.ColoredString( "----- Artifacts -----", Color.WHITE), ()->{});
            for(Artifact artifact : context.getInventory().getArtifacts()){
                put(renderItem(artifact), ()->{});
            }
            put(new OptionsWidget.ColoredString( "----- Scrolls -----", Color.WHITE), ()->{});
            for(Scroll scroll : context.getInventory().getScrolls()){
                put(renderItem(scroll), ()->{});
            }

            put(new OptionsWidget.ColoredString("----- Backpack -----", Color.WHITE), () -> {});

         for(Item item : context.getInventory().getBackpackItems()){
             put(renderItem(item), () -> {

             });
         }

        }});

        addChild(itemList);


    }



    public OptionsWidget.ColoredString renderItem(Item item){


        String string = "";
        int color = Color.WHITE;
        if(item instanceof Scroll){
            string += "[~] ";
            color = ((Scroll) item).getSpell().getMagicType().getColor();
            string += ((Scroll) item).getSpell().getMagicType().getSymbol() + " ";
        }
        if(item instanceof Armor){
            string += "[=] ";
        }
        if(item instanceof Staff){
            string += "/* ";
        }
        if(item instanceof Artifact){
            string += "@ ";
        }

        if(item instanceof Potion){
            string += "(o) ";
            string += ((Potion) item).getMagicType().getSymbol() + " ";
            color += ((Potion) item).getMagicType().getColor();
        }

        if(item instanceof MagicItem){
            string += ((MagicItem) item).getMagicType().getSymbol() +" ";
            color = ((MagicItem) item).getMagicType().getColor();
        }






        string += item.getName();
        return new OptionsWidget.ColoredString(string , color);
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
