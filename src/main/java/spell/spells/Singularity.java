package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class Singularity extends Spell {
    public Singularity() {
        super("Singularity", "Condenses spacetime into an inescapable gravitational singularity that crushes enemy strength and mobility while shielding you.", MagicType.INFINITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(150, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 4, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 4, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 3, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
