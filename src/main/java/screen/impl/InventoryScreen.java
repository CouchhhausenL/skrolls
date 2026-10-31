package main.java.screen.impl;


import main.java.MagicType;
import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.item.*;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.ItemEffect;
import main.java.item.items.*;
import main.java.screen.Screen;
import main.java.screen.widget.impl.ChatMessageWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.container.ScrollableContainerWidget;
import main.java.screen.widget.impl.container.VerticalContainerWidget;
import main.java.screen.widget.impl.options.ItemOption;
import main.java.screen.widget.impl.options.Option;
import main.java.spell.Spell;
import main.java.util.Color;
import main.java.util.SplashText;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

// Placeholder inventory screen; closing returns to the main game screen
public class InventoryScreen extends Screen {

    private GameContext context;
    private OptionsWidget itemList;

    private ScrollableContainerWidget detailContainer = new ScrollableContainerWidget(41,0,40,30,0);

    private LabelWidget detailedMagicType;

    private LabelWidget hintWidget;




    @Override
    protected void init(GameContext context) {
        this.context = context;
        detailContainer.setKeyboardInput(false);
        detailContainer.setOnlyScrollOnOverflow(true);

        detailContainer.addChild( new LabelWidget(41, 0, "Select Something to see details!"));
        detailContainer.addChild( new ChatMessageWidget(41, 2, 40,"You can press [Space] to select an item!"));
        detailContainer.addChild( new LabelWidget(41, 1, "---------------"));

        addChild(detailContainer);

        hintWidget = new LabelWidget(0,getHeight() - 1,"Press [e] to equip, [u] to unequip, and [s] to sacrifice!");
        addChild(hintWidget);

        drawInventory();
    }

    public void drawInventory() {
        // Remove old itemList before replacing it
        if (itemList != null) {
            removeChild(itemList);
        }

        itemList = new OptionsWidget(0, 0, 40, getHeight() - 1);
        itemList.addOption(new Option("----- Staff -----", Color.BEIGE, null));

        ItemOption staffOption = renderItem(context.getInventory().getStaff(), () -> showDetails(context.getInventory().getStaff()));
        if (staffOption != null) itemList.addOption(staffOption);

        itemList.addOption(new Option("----- Armor -----", Color.BEIGE, null));

        ItemOption armorOption = renderItem(context.getInventory().getArmor(), () -> showDetails(context.getInventory().getArmor()));
        if (armorOption != null) itemList.addOption(armorOption);

        itemList.addOption(new Option("----- Artifacts -----", Color.BEIGE, null));
        for (Artifact artifact : context.getInventory().getArtifacts()) {
            ItemOption artifactOption = renderItem(artifact, () -> showDetails(artifact));
            if (artifactOption != null) itemList.addOption(artifactOption);
        }
        itemList.addOption(new Option("----- Skrolls -----", Color.BEIGE, null));
        for (Scroll scroll : context.getInventory().getScrolls()) {
            ItemOption artifactOption = renderItem(scroll, () -> showDetails(scroll));
            if (artifactOption != null) itemList.addOption(artifactOption);
        }

        itemList.addOption(new Option("----- Backpack -----", Color.BEIGE, null));
        for (Item item : context.getInventory().getBackpackItems()) {
            ItemOption backpackOption = renderItem(item, () -> showDetails(item));
            if (backpackOption != null) itemList.addOption(backpackOption);
        }

        addChild(itemList);


    }

    public void showDetails(Item item){
        showDetails(item, detailContainer);
    }

    public static void showDetails(Item item, ScrollableContainerWidget detailContainer) {

        detailContainer.clearChildren();

        detailContainer.setKeyboardInput(false);
        detailContainer.setOnlyScrollOnOverflow(true);


        detailContainer.addChild( new LabelWidget(41, 0, item.getName(), ItemHelper.getItemColor(item)));
        detailContainer.addChild(new LabelWidget(41, 1, "-".repeat(item.getName().length())));
        detailContainer.addChild( new ChatMessageWidget(41, 2, 40, item.getDescription()));


        detailContainer.addChild( new LabelWidget(41, detailContainer.getY() + detailContainer.getHeight() + 1, "MagicType:" + ItemHelper.getMagicTypeName(item)));




        if (item instanceof MagicItem) {
            List<ItemEffect> itemEffects = ((MagicItem) item).getItemEffects();


            for (ItemEffect itemEffect : itemEffects) {
                LabelWidget itemEffectLabel = new LabelWidget(0, 0, itemEffect.itemEffectType().toString() + ", " + itemEffect.effectStrength());
                detailContainer.addChild(itemEffectLabel);
            }
        } else if (item instanceof Potion) {
            List<CombatEffect> potionEffects = ((Potion) item).getCombatEffects();


            for (CombatEffect combatEffect : potionEffects) {
                LabelWidget combatEffectLabel = new LabelWidget(0, 0, combatEffect.getCombatEffectType().toString() + ", " + combatEffect.getEffectStrength()
                        + ", " + combatEffect.getDuration());
                detailContainer.addChild(combatEffectLabel);

            }
        }else if(item instanceof Scroll scroll){
            Spell spell = scroll.getSpell();
            detailContainer.addChild(new LabelWidget(0,0, "Base Damage: " + spell.getBaseDamage()));
            detailContainer.addChild(new LabelWidget(0,0, "Mana Cost: " + spell.getManaCost()));

                for (CombatEffect playerEffect : spell.getPlayerEffects()) {
                    detailContainer.addChild(new LabelWidget(0, 0, playerEffect.getCombatEffectType().toString() + ", " + playerEffect.getEffectStrength(), Color.GREEN));
                }


                for (CombatEffect enemyEffect : spell.getEnemyEffects()) {
                    detailContainer.addChild(new LabelWidget(0, 0, enemyEffect.getCombatEffectType().toString() + ", " + enemyEffect.getEffectStrength(), Color.RED));

                }


        }




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
        if (event.isEscape()){
                context.setScreen(new InventoryScreenCloseTransition());
            }



        if (event.isChar('e') || event.isChar('E')) {
            if (itemList.getSelectedOption() instanceof ItemOption itemOption) {
                if (itemOption.getItem() instanceof Equippable equippable) {
                    context.equipItem(equippable);
                    drawInventory();
                }
            }
        }


        if (event.isChar('s')) {
            if (itemList.getSelectedOption() instanceof ItemOption itemOption) {
                    context.sacrificeItem(itemOption.getItem());
                    drawInventory();

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
