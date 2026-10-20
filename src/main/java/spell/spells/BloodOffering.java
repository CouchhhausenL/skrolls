package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class BloodOffering extends Spell {
    public BloodOffering() {
        super("Blood Offering", "Tears open deep necrotic gashes in the foe, causing bleeding while intoxicating you with critical focus and strength.", MagicType.NECROTIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(85, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 20, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
