package main.java.character;

import main.java.item.Armor;
import main.java.item.Artifact;
import main.java.item.Item;
import main.java.item.Staff;

import java.util.List;

public class Inventory {
    private List<Item> backpack;
    private Armor armor;
    private Staff staff;
    private List<Artifact> artifacts;

    public void addItem(Item item){
        backpack.add(item);
    }
    public void addItems(List<Item> items){
        for(Item item: items){
            addItem(item);
        }
    }

    public String equipStaff(Staff staff){
        Staff oldStaff = this.staff;
        backpack.remove(staff);
        backpack.add(oldStaff);
        this.staff = staff;
        return  "You've equipped '" + staff.getName() + "' and stashed '" +  oldStaff.getName() + "' in your backpack.";
    }

    public String equipArmor(Armor armor){
        Armor oldArmor = this.armor;
        backpack.remove(armor);
        backpack.add(oldArmor);
        this.armor = armor;
        return  "You've equipped '" + armor.getName() + "' and stashed '" +  oldArmor.getName() + "' in your backpack.";
    }

    public String equipArtifact(Artifact artifact){
        if(artifacts.size() >= 10){
        return "The artifact '" + artifact.getName() + "' can't be equipped, because you have already have 10 artifacts equipped.";
        } else {
            backpack.remove(artifact);
            artifacts.add(artifact);
        return "You've equipped '" + artifact.getName() + "'.";
        }
    }

    public List<Item> getBackpackItems(){
        return List.copyOf(backpack);
    }
    public Staff getStaff(){
        return staff;
    }
    public Armor getArmor(){
        return armor;
    }
    public List<Artifact> getArtifacts(){
        return List.copyOf(artifacts);
    }

}
