package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class Singularity extends Spell {
    public Singularity() {
        super("Singularity", "Condenses spacetime into an inescapable gravitational singularity. Deals 150 Infinity damage, crushes enemy Speed (-4 for 4 rounds) and Attack (-4 for 4 rounds), and wraps you in gravitational Resistance (+3 Defense for 3 rounds). Costs 190 Mana.", MagicType.INFINITY);
    }

    @Override
    public int getBaseDamage() {
        return 150;
    }

    @Override
    public int getManaCost() {
        return 190;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.SLOWNESS, 4, 4),
                new CombatEffect(CombatEffectType.WEAKNESS, 4, 4)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.RESISTANCE, 3, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
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
