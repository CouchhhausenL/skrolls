package main.java.screen.impl;


import main.java.MagicType;
import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.item.*;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.ItemEffect;
import main.java.item.items.*;
import main.java.screen.Screen;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.container.VerticalContainerWidget;
import main.java.screen.widget.impl.options.ItemOption;
import main.java.screen.widget.impl.options.Option;
import main.java.util.Color;
import main.java.util.SplashText;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

// Placeholder inventory screen; closing returns to the main game screen
public class InventoryScreen extends Screen {

    private GameContext context;
    private OptionsWidget itemList;
    private LabelWidget detailedName;
    private LabelWidget detailedSeperator;
    private LabelWidget detailedDescription;
    private LabelWidget detailedMagicType;

    private LabelWidget hintWidget;

    private VerticalContainerWidget detailedEffectContainer;


    @Override
    protected void init(GameContext context) {
        this.context = context;

        detailedName = new LabelWidget(41, 0, "Select Something to see details!");
        detailedDescription = new LabelWidget(41, 2, "You can press [Space] to select an item!");
        detailedSeperator = new LabelWidget(41, 1, "---------------");
        addChild(detailedName);
        addChild(detailedDescription);
        addChild(detailedSeperator);

        hintWidget = new LabelWidget(0,getHeight(),"Press [e] to equip and [u] to unequip!");

        drawInventory();
    }

    public void drawInventory() {
        // Remove old itemList before replacing it
        if (itemList != null) {
            removeChild(itemList);
        }

        itemList = new OptionsWidget(0, 0, 40, getHeight() - 1);
        itemList.addOption(new Option("----- Staff -----", 0x9e57b, null));

        ItemOption staffOption = renderItem(context.getInventory().getStaff(), () -> showDetails(context.getInventory().getStaff()));
        if (staffOption != null) itemList.addOption(staffOption);

        itemList.addOption(new Option("----- Armor -----", 0x9e57b, null));

        ItemOption armorOption = renderItem(context.getInventory().getArmor(), () -> showDetails(context.getInventory().getArmor()));
        if (armorOption != null) itemList.addOption(armorOption);

        itemList.addOption(new Option("----- Artifacts -----", 0x9e57b, null));
        for (Artifact artifact : context.getInventory().getArtifacts()) {
            ItemOption artifactOption = renderItem(artifact, () -> showDetails(artifact));
            if (artifactOption != null) itemList.addOption(artifactOption);
        }

        itemList.addOption(new Option("----- Backpack -----", 0x9e57b, null));
        for (Item item : context.getInventory().getBackpackItems()) {
            ItemOption backpackOption = renderItem(item, () -> showDetails(item));
            if (backpackOption != null) itemList.addOption(backpackOption);
        }

        addChild(itemList);


    }



    public void showDetails(Item item) {
        removeChild(detailedName);
        removeChild(detailedDescription);
        removeChild(detailedSeperator);
        removeChild(detailedMagicType);
        removeChild(detailedEffectContainer);

        detailedName = new LabelWidget(41, 0, item.getName(), ItemHelper.getItemColor(item));
        detailedSeperator = new LabelWidget(41, 1, "-".repeat(item.getName().length()));
        detailedDescription = new LabelWidget(41, 2, item.getDescription());

        detailedEffectContainer = new VerticalContainerWidget(41, detailedDescription.getHeight() + 3, 40, 0, 0);
        detailedEffectContainer.clearChildren();

        if (item instanceof MagicItem) {
            List<ItemEffect> itemEffects = ((MagicItem) item).getItemEffects();
            detailedEffectContainer.setHeight(itemEffects.size());
            int yIncrement = 0;
            for (ItemEffect itemEffect : itemEffects) {
                LabelWidget itemEffectLabel = new LabelWidget(0, yIncrement, itemEffect.itemEffectType().toString() + ", " + itemEffect.effectStrength());
                detailedEffectContainer.addChild(itemEffectLabel);
                yIncrement++;
            }
        } else if (item instanceof Potion) {
            List<CombatEffect> potionEffects = ((Potion) item).getCombatEffects();
            detailedEffectContainer.setHeight(potionEffects.size());
            int yIncrement = 0;
            for (CombatEffect combatEffect : potionEffects) {
                LabelWidget combatEffectLabel = new LabelWidget(0, yIncrement, combatEffect.getCombatEffectType().toString() + ", " + combatEffect.getEffectStrength()
                        + ", " + combatEffect.getDuration());
                detailedEffectContainer.addChild(combatEffectLabel);
                yIncrement++;
            }
        }
        detailedMagicType = new LabelWidget(41, detailedEffectContainer.getY() + detailedEffectContainer.getHeight() + 1, "MagicType:" + ItemHelper.getMagicTypeName(item));

        addChild(detailedEffectContainer);
        addChild(detailedName);
        addChild(detailedDescription);
        addChild(detailedSeperator);
        addChild(detailedMagicType);
    }


    public ItemOption renderItem(Item item, Runnable runnable) {
        if (item == null) {
            return null;
        }
        String string = ItemHelper.getItemTypeIcon(item) + " " + ItemHelper.getItemMagicTypeSymbol(item) + " " + item.getName();
        return new ItemOption(string, ItemHelper.getItemColor(item), item, runnable);
    }


    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isEscape())
            if( context.getInventory().getStaff() == null || context.getInventory().getArmor() == null){
                MessageScreen messageScreen = new MessageScreen();
                messageScreen.setMessage(SplashText.getNo() +"\n"+"You have to equip a Staff and a Armour!");
                context.setScreen(messageScreen);
                messageScreen.setOnComplete(()-> context.setScreen(new InventoryScreen()));
            }else {
                context.setScreen(new MainGameScreen());
            }

        if (event.isChar('e') || event.isChar('E')) {
            if (itemList.getSelectedOption() instanceof ItemOption itemOption) {
                if (itemOption.getItem() instanceof Equippable equippable) {
                    context.equipItem(equippable);
                    drawInventory();
                }
            }
        }

        if (event.isChar('u')) {
            if (itemList.getSelectedOption() instanceof ItemOption itemOption) {
                if (itemOption.getItem() instanceof Equippable equippable) {
                    context.unequipItem(equippable);
                    drawInventory();
                }
            }
        }
    }

    @Override
    public void onResize() {

    }
}
