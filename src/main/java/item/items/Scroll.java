package main.java.item.items;

import main.java.MagicType;
import main.java.item.*;
import main.java.spell.Spell;

// Equippable wrapper that lets the player cast the contained spell.
// The name and description are derived from the spell itself.
public class Scroll extends AbstractItem implements Equippable, Sacrificeable {
    private final Quality quality;
    private final Spell spell;

    public Scroll(Spell spell, Quality quality) {
        super(spell.getName() + " Scroll", "The description says: " + spell.getDescription());
        this.quality = quality;
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    public MagicType getMagictype(){
        return spell.getMagicType();
    }

    @Override
    public EquipmentType<Scroll> getEquipmentType() {
        return EquipmentType.SCROLL;
    }

    @Override
    public Quality getQuality() {
        return quality;
    }
}
