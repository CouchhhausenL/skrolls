package main.java.item.items;

import main.java.item.Item;
import main.java.item.Quality;
import main.java.spell.Spell;

public class Scroll extends Item {
    private final Quality quality;
    private final Spell spell;


    public Scroll(Spell spell, Quality quality) {
        super(spell.getName() + " Scroll", "The description says: " + spell.getDescription());
        this.quality = quality;
        this.spell = spell;


    }


}
