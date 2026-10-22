package main.java.item;

import main.java.MagicType;
import main.java.item.items.*;
import main.java.util.Color;

public class ItemHelper {
    public static String getItemTypeIcon(Item item){

        if (item instanceof Staff){
            return "/*";
        } else if(item instanceof Armor){
            return "{=}";
        } else if (item instanceof Artifact) {
            return "@";
        } else if (item instanceof Scroll) {
            return "[~]";
        } else if (item instanceof Potion) {
            return "(o)";
        } else {
            return "";
        }
    }
    public static MagicType getMagicTypeOfItem(Item item){
        if(item instanceof MagicItem){
            return ((MagicItem) item).getMagicType();
        } else if (item instanceof Scroll) {
            return ((Scroll) item).getMagictype();
        } else if (item instanceof Potion) {
            return ((Potion) item).getMagicType();
        }
        else{return null;}

    }
    public static int getItemColor(Item item){
        if(getMagicTypeOfItem(item) != null){
            return getMagicTypeOfItem(item).getColor();
        }else {
            return Color.WHITE;
        }
    }
    public static String getItemMagicTypeSymbol(Item item){
        if(getMagicTypeOfItem(item) != null){
            return getMagicTypeOfItem(item).getSymbol();
        } else {
            return "";
        }
    }
    public static String getMagicTypeName(Item item){
        if(getItemMagicTypeSymbol(item) != null){
            return getMagicTypeOfItem(item).getDisplayName();
        } else {
            return "None";
        }
    }
}
