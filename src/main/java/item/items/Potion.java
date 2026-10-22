package main.java.item.items;

import main.java.MagicType;
import main.java.item.AbstractItem;
import main.java.item.Quality;
import main.java.item.effects.CombatEffect;

import java.util.List;

// Consumable that applies its combat effects when drunk.
// Fields are public because only Entity.drinkPotion() reads them.
public class Potion extends AbstractItem {
    public final Quality quality;
    public final MagicType magicType;
    public final List<CombatEffect> combatEffects;

    public Potion(String name, String description, Quality quality, MagicType magicType, List<CombatEffect> combatEffects) {
        super(name, description);
        this.quality = quality;
        this.magicType = magicType;
        this.combatEffects = combatEffects;
    }

    public MagicType getMagicType() {
        return magicType;
    }

    public List<CombatEffect> getCombatEffects(){
        return combatEffects;
    }
}
