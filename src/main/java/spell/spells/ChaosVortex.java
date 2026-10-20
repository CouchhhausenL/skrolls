package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ChaosVortex extends Spell {
    public ChaosVortex() {
        super("Chaos Vortex", "Rips open a swirling vortex of turbulent chaos, inflicting burns, bewildering confusion, and shattered defenses.", MagicType.CHAOS);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(135, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.5, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 20, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
