package main.java.item;

import main.java.item.effects.ItemEffectType;
import main.java.item.items.Armor;
import main.java.item.items.Artifact;
import main.java.item.items.Scroll;
import main.java.item.items.Staff;
import main.java.verb.Verb;
import main.java.verb.VerbSupplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Player's possessions: a backpack of unequipped items plus the equipped
// items managed by the EquipmentStore.
public class Inventory implements VerbSupplier {
    private final List<Item> backpack = new ArrayList<>();

    public EquipmentStore getEquipment() {
        return equipment;
    }

    private final EquipmentStore equipment = new EquipmentStore();

    public Inventory() {
    }

    public void addItem(Item item) {
        backpack.add(item);
    }

    public void addItems(List<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    public void removeItem(Item item) {
        backpack.remove(item);
    }

    public List<Item> getBackpackItems() {
        return Collections.unmodifiableList(backpack);
    }

    public Staff getStaff() {
        return equipment.getEquipable(EquipmentType.STAFF);
    }

    public Armor getArmor() {
        return equipment.getEquipable(EquipmentType.ARMOR);
    }

    public List<Artifact> getArtifacts() {
        return equipment.getEquipables(EquipmentType.ARTIFACT);
    }

    public List<Scroll> getScrolls() {
        return equipment.getEquipables(EquipmentType.SCROLL);
    }

    // Sums an effect over all equipped items (backpack items do not count)
    public double getCombinedEffect(ItemEffectType itemEffectType) {
        double combinedEffect = 0;
        Staff staff = getStaff();
        if (staff != null) {
            combinedEffect += staff.getItemEffectStrength(itemEffectType);
        }
        Armor armor = getArmor();
        if (armor != null) {
            combinedEffect += armor.getItemEffectStrength(itemEffectType);
        }
        for (Artifact artifact : getArtifacts()) {
            combinedEffect += artifact.getItemEffectStrength(itemEffectType);
        }
        return combinedEffect;
    }

    // Returns the higher of the current maximum and the item's quality ordinal
    private int checkItemQuality(Item item, int currentHighest) {
        if (item instanceof MagicItem magicItem) {
            int ordinal = magicItem.getQuality().ordinal();
            if (ordinal > currentHighest) {
                return ordinal;
            }
        }
        return currentHighest;
    }

    // Highest quality across backpack and all equipped items.
    // Non-magic items (e.g. potions) are ignored.
    public Quality getHighestQualityItem() {
        int highestOrdinal = -1; // Starting value below any valid quality ordinal

        for (Item item : backpack) {
            highestOrdinal = checkItemQuality(item, highestOrdinal);
        }

        for (Item item : getArtifacts()) {
            highestOrdinal = checkItemQuality(item, highestOrdinal);
        }

        highestOrdinal = checkItemQuality(getArmor(), highestOrdinal);
        highestOrdinal = checkItemQuality(getStaff(), highestOrdinal);
        return Quality.values()[highestOrdinal];
    }

    // Moves an item from the backpack into its equipment slot.
    // Fails if the item is not equippable, not in the backpack or the slot is full.
    public boolean equipItem(Item item) {
        if (!(item instanceof Equippable equippable)) return false;

        if (!backpack.contains(equippable)) {
            return false;
        }

        boolean equipped = equipment.equip(equippable);
        if (equipped) {
            backpack.remove(equippable);
            return true;
        }

        return false;
    }


    public boolean unequipItem(Item item) {
        if (!(item instanceof Equippable equippable)) return false;

        if (equipment.unequip(equippable)) {
            backpack.add(equippable);
            return true;
        }
        return false;
    }


    // Unequip verbs for equipped items plus one "equip <item>" verb per
    // equippable item in the backpack
    @Override
    public List<Verb> getVerbs() {
        List<Verb> verbs = new ArrayList<>();

        verbs.addAll(equipment.getVerbs());

        for (Item item : backpack) {

            if (item instanceof Equippable equippable) {
                String name = equippable.getName();

                Verb.Builder builder = Verb.builder("equip")
                        .aliases("wear");

                builder.variant().choice("item", name).build().build();

                verbs.add(builder.build(((executionContext, context) -> {
                    if (equipItem(equippable)) {
                        context.addMessage("Equipped " + name);
                    } else {
                        context.addMessage("Failed to equip " + name);
                    }
                })));
            }
        }

        return verbs;
    }
}
