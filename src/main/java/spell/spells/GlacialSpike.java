package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class GlacialSpike extends Spell {
    public GlacialSpike() {
        super("Glacial Spike", "Hurls a massive pillar of freezing ice that damages, freezes, and weakens the enemy.", MagicType.ELEMENTAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(75, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.FROZEN, 0, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
