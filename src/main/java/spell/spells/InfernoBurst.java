package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class InfernoBurst extends Spell {
    public InfernoBurst() {
        super("Inferno Burst", "Engulfs the target in scorching flames that continue burning each turn.", MagicType.ELEMENTAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(80, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 15, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
