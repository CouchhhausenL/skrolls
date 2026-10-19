package main.java.entity.player;

import main.java.DifficultyLevel;
import main.java.MagicType;
import main.java.entity.Entity;
import main.java.item.Inventory;
import main.java.item.effects.ItemEffectType;

public class Player extends Entity {
    private int mana;
    private int maxMana;
    private int magicShards;
    private final Inventory inventory;


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


    public double getDamageItemFactor() {
        return inventory.getCombinedEffect(ItemEffectType.DAMAGE);
    }

    public double getBaseDamageItemFactor() {
        return inventory.getCombinedEffect(ItemEffectType.OWN_MAGIC);

    }

    public double getDefenseItemFactor(MagicType magicType) {
        ItemEffectType searchType = switch (magicType) {
            case KINETIC -> ItemEffectType.RESISTANCE_KINETIC;
            case ELEMENTAL -> ItemEffectType.RESISTANCE_ELEMENTAL;
            case PURITY -> ItemEffectType.RESISTANCE_PURITY;
            case ABYSSAL -> ItemEffectType.RESISTANCE_ABYSSAL;
            case NECROTIC -> ItemEffectType.RESISTANCE_NECROTIC;
            case DRAGONIC -> ItemEffectType.RESISTANCE_DRAGONIC;
            case ARCANE -> ItemEffectType.RESISTANCE_ARCANE;
            case CHAOS -> ItemEffectType.RESISTANCE_CHAOS;
            case INFINITY -> null;
        };
        if (searchType == null) {
            return 1.0;
        }

        return inventory.getCombinedEffect(searchType);
    }

    @Override
    public int getDefenseStat() {
        return (int) Math.floor(inventory.getCombinedEffect(ItemEffectType.DEFENSE));
    }

    @Override
    public MagicType getAttackingMagicType() {
        return inventory.getStaff().getMagicType();
    }

    @Override
    public int getLevel() {
        return DifficultyLevel.values()[inventory.getStaff().getQuality().ordinal()].getLevel();
    }

    public void maxManaPercentageDrain(double percentage){
        maxMana = (int) Math.floor(maxMana - maxMana *  percentage);
    }


    public void gainMana(int amount){
        mana = Math.min(mana + amount, maxMana);
    }
    public void spendMana(int amount){
        mana = Math.max(0, mana - amount);
    }
    public void regenerateMana(double percentage){
        mana = (int) Math.min(Math.floor(mana + (maxMana * percentage / 100)), maxMana);
    }


    public Inventory getInventory() {
        return inventory;
    }

    public DifficultyLevel getDifficultyLevel() {
        return DifficultyLevel.values()[inventory.getHighestQualityItem().ordinal()];
    }


    @Override
    public MagicType getDefendingMagicType() {
        return inventory.getArmor().getMagicType();
    }
}
