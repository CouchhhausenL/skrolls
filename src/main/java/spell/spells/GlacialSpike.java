package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class GlacialSpike extends Spell {
    public GlacialSpike() {
        super("Glacial Spike", "Hurls a massive pillar of freezing ice into the enemy. Deals 75 Elemental damage, freezes the enemy for 3 rounds, and weakens their Attack (-2 for 2 rounds). Costs 45 Mana.", MagicType.ELEMENTAL);
    }

    @Override
    public int getBaseDamage() {
        return 75;
    }

    @Override
    public int getManaCost() {
        return 45;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.FROZEN, 0, 3),
                new CombatEffect(CombatEffectType.WEAKNESS, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.FROZEN, 0, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
