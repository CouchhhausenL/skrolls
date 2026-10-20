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

    private int checkItemQuality(Item item, int currentHighest) {
        if (item instanceof MagicItem magicItem) {
            int ordinal = magicItem.getQuality().ordinal();
            if (ordinal > currentHighest) {
                return ordinal;
            }
        }
        return currentHighest;
    }

    public Quality getHighestQualityItem() {
        int highestOrdinal = -1; // Oder dein bisheriger Initialwert

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

    @Override
    public List<Verb> getVerbs() {
        List<Verb> verbs = new ArrayList<>();

        verbs.addAll(equipment.getVerbs());

        for (Item item : backpack) {

            if (item instanceof Equipable equipable) {
                Verb.Builder builder = Verb.builder("equip")
                        .aliases("wear");

                builder.variant().choice("item", equipable.getName());

                verbs.add(builder.build(((executionContext, context) -> {
                    removeItem(equipable);
                    equipment.equip(equipable);
                })));
            }
        }

        return verbs;
    }
}
