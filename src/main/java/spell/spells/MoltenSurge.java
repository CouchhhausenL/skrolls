package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class MoltenSurge extends Spell {
    public MoltenSurge() {
        super("Molten Surge", "Erupts molten lava beneath the foe, inflicting continuous burns and melting their armor.", MagicType.ELEMENTAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(95, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 20, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 1, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
