package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class AuraOfRenewal extends Spell {
    public AuraOfRenewal() {
        super("Aura of Renewal", "Bathes you in celestial light, granting health restoration over time.", MagicType.PURITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 20, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.REGENERATION, 15, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "magic.ska";
    }
}
