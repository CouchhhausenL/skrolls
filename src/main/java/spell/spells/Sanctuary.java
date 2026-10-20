package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class Sanctuary extends Spell {
    public Sanctuary() {
        super("Sanctuary", "Creates a sacred haven that fortifies resistance, bestows prolonged regeneration, and restores mana.", MagicType.PURITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.REGENERATION, 25, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_REGEN, 15, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 2, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "magic.ska";
    }
}
