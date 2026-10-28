package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class EntropyOverload extends Spell {
    public EntropyOverload() {
        super("Entropy Overload", "Discharges erratic entropic energy, collapsing hostile order while empowering your form. Deals 140 Chaos damage, dispels all enemy periodic buffs, reduces enemy Attack (-3 for 2 rounds), and boosts your Attack (+4 for 2 rounds) and Speed (+2 for 2 rounds). Costs 150 Mana.", MagicType.CHAOS);
    }

    @Override
    public int getBaseDamage() {
        return 140;
    }

    @Override
    public int getManaCost() {
        return 150;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.DISPEL, 0, 0),
                new CombatEffect(CombatEffectType.WEAKNESS, 3, 2)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.STRENGTH, 4, 2),
                new CombatEffect(CombatEffectType.SPEED, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 3, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 4, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.SPEED, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
