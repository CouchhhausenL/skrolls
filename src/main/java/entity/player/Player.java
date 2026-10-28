package main.java.entity.player;

import main.java.Converter;
import main.java.DifficultyLevel;
import main.java.MagicType;
import main.java.PlayerLevel;
import main.java.entity.Entity;
import main.java.item.Inventory;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.ItemEffect;
import main.java.item.effects.ItemEffectType;
import main.java.item.items.Potion;

// Player stats are derived from equipped items in the inventory;
// base values come from Entity and item effects are added on top.
public class Player extends Entity {
    private int mana;
    private int maxMana;

    private int magicShards;
    private final Inventory inventory;

    private PlayerLevel playerLevel;


    public Player(String name, PlayerLevel playerLevel, Inventory inventory) {
        super(name, playerLevel.getBaseMaxHealth(), playerLevel.getBaseSpeed());
        this.playerLevel = playerLevel;
        this.inventory = inventory;
        this.magicShards = 100;
        this.maxMana = playerLevel.getBaseMaxMana();
    }

    @Override
    public int getAttackStat() {
        return playerLevel.getBaseAttack() + (int) inventory.getCombinedEffect(ItemEffectType.ATTACK);
    }


    public double getDamageItemFactor() {
        double combined = inventory.getCombinedEffect(ItemEffectType.DAMAGE);
        return combined == 0.0 ? 1.0 : combined;
    }

    public double getBaseDamageItemFactor() {
        double combined = inventory.getCombinedEffect(ItemEffectType.OWN_MAGIC);
        return combined == 0.0 ? 1.0 : combined;

    }

    // Maps the incoming magic type to the matching resistance item effect
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
        // Infinity has no resistance item effect -> neutral multiplier
        if (searchType == null) {
            return 1.0;
        }

        double combined = inventory.getCombinedEffect(searchType);
        return combined == 0.0 ? 1.0 : combined;
    }

    public PlayerLevel getPlayerLevel() {
        return playerLevel;
    }

    @Override
    public int getDefenseStat() {
        return playerLevel.getBaseDefense() + (int) Math.floor(inventory.getCombinedEffect(ItemEffectType.DEFENSE));
    }


    @Override
    public int getMaxHealth() {
        return super.getMaxHealth() + (int) inventory.getCombinedEffect(ItemEffectType.MAX_HEALTH);
    }

    @Override
    public int getSpeed() {
        return super.getSpeed() + (int) inventory.getCombinedEffect(ItemEffectType.SPEED);
    }

    @Override
    public MagicType getAttackingMagicType() {
        if (inventory.getStaff() != null) {
            return inventory.getStaff().getMagicType();
        } else {
            return MagicType.KINETIC;
        }

    }


    @Override
    public int getLevel() {
        return playerLevel.getLevel();
    }

    public int getMaxMana() {
        return maxMana + (int) inventory.getCombinedEffect(ItemEffectType.MAX_MANA);
    }


    public void maxManaPercentageDrain(double percentage) {
        maxMana = (int) Math.floor(maxMana - maxMana * percentage);
    }


    public void gainMana(int amount) {
        mana = Math.min(mana + amount, maxMana);
    }

    public void spendMana(int amount) {
        mana = Math.max(0, mana - amount);
    }

    public void roundlyRegenerateMana() {
        regenerateMana(playerLevel.getManaRegen());
    }

    public void regenerateMana(double percentage) {
        mana = (int) Math.min(Math.floor(mana + (maxMana * percentage / 100)), maxMana);
    }

    public Inventory getInventory() {
        return inventory;
    }


    public DifficultyLevel getDifficultyLevel() {
        return Converter.PlayerLevelToDifficultyLevel(playerLevel);
    }

    public void drinkPotion(Potion potion) {
        for (CombatEffect combatEffect : potion.getCombatEffects()) {
            addCombatEffect(combatEffect);
            inventory.removeItem(potion);
        }

    }

    public void upgradePlayerLevel() {
        if (playerLevel.ordinal() >= PlayerLevel.values().length) {
            return;
        }
        playerLevel = PlayerLevel.values()[playerLevel.ordinal() + 1];

        this.maxMana = playerLevel.getBaseMaxMana();
        super.maxHealth = playerLevel.getBaseMaxHealth();
    }


    public int getMagicShards() {
        return magicShards;
    }

    public void setMagicShards(int magicShards) {
        this.magicShards = magicShards;
    }

    @Override
    public MagicType getDefendingMagicType() {
        if (inventory.getArmor() != null) {
            return inventory.getArmor().getMagicType();
        } else {
            return MagicType.KINETIC;
        }

    }
}
