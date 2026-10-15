package main.java.entity.player;

import main.java.DifficultyLevel;
import main.java.MagicType;
import main.java.entity.Entity;
import main.java.item.effects.ItemEffectType;

public class Player extends Entity {
    private int mana;
    private int maxMana;
    private int magicShards;
    private Inventory inventory;


    public Player(String name, int maxHealth, int speed, Inventory inventory) {
        super(name, maxHealth, speed);
        this.inventory = inventory;
    }

    public void cast() {

    }

    public void move() {

    }

    @Override
    public int getAttackStat() {
        return (int) inventory.getCombinedEffect(ItemEffectType.ATTACK);
    }




    public double getDamageItemFactor(){
        return inventory.getCombinedEffect(ItemEffectType.DAMAGE_BOOST);
    }

    public double getBaseDamageItemFactor(){
        return inventory.getCombinedEffect(ItemEffectType.BUFFS_OWN_MAGIC);

    }




    public Inventory getInventory(){
        return inventory;
    }

    public DifficultyLevel getDifficultyLevel(){
        return DifficultyLevel.values()[inventory.getHighestQualityItem().ordinal()];
    }


    @Override
    public MagicType getDefendingMagicType() {
        return inventory.getArmor().getMagicType();
    }
}
