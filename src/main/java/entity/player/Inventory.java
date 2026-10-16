package main.java.entity.player;

import main.java.item.Item;
import main.java.item.MagicItem;
import main.java.item.Quality;
import main.java.item.effects.ItemEffectType;
import main.java.item.items.Armor;
import main.java.item.items.Artifact;
import main.java.item.items.Staff;

import java.util.List;

public class Inventory {
    private List<Item> backpack;
    private Armor armor;
    private Staff staff;
    private List<Artifact> artifacts;

    public void addItem(Item item) {
        backpack.add(item);
    }

    public void addItems(List<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    public String equipStaff(Staff staff) {
        Staff oldStaff = this.staff;
        backpack.remove(staff);
        backpack.add(oldStaff);
        this.staff = staff;
        return "You've equipped '" + staff.getName() + "' and stashed '" + oldStaff.getName() + "' in your backpack.";
    }

    public String equipArmor(Armor armor) {
        Armor oldArmor = this.armor;
        backpack.remove(armor);
        backpack.add(oldArmor);
        this.armor = armor;
        return "You've equipped '" + armor.getName() + "' and stashed '" + oldArmor.getName() + "' in your backpack.";
    }

    public String equipArtifact(Artifact artifact) {
        if (artifacts.size() >= 10) {
            return "The artifact '" + artifact.getName() + "' can't be equipped, because you have already have 10 artifacts equipped.";
        } else {
            backpack.remove(artifact);
            artifacts.add(artifact);
            return "You've equipped '" + artifact.getName() + "'.";
        }
    }

    public List<Item> getBackpackItems() {
        return List.copyOf(backpack);
    }

    public Staff getStaff() {
        return staff;
    }

    public Armor getArmor() {
        return armor;
    }

    public List<Artifact> getArtifacts() {
        return List.copyOf(artifacts);
    }

    public double getCombinedEffect(ItemEffectType itemEffectType) {
        double combinedEffect = 0;
        for (Artifact artifact : artifacts) {
            combinedEffect = combinedEffect + artifact.getItemEffectStrength(itemEffectType);
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

        for (Item item : artifacts) {
            highestOrdinal = checkItemQuality(item, highestOrdinal);
        }

        highestOrdinal = checkItemQuality(armor, highestOrdinal);
        highestOrdinal = checkItemQuality(staff, highestOrdinal);
        return Quality.values()[highestOrdinal];
    }


}
