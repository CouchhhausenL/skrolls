package main.java.item.items;

import main.java.MagicType;
import main.java.item.Item;
import main.java.item.Quality;
import main.java.item.effects.CombatEffect;

import java.util.List;

public class Potion extends Item {
    public final Quality quality;
    public final MagicType magicType;
    public final List<CombatEffect> combatEffects;

    public Potion(String name, String description, Quality quality, MagicType magicType, List<CombatEffect> combatEffects) {
        super(name, description);
        this.quality = quality;
        this.magicType = magicType;
        this.combatEffects = combatEffects;
    }
}
