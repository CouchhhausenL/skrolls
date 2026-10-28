package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class HeavyShieldBash extends Spell {
    public HeavyShieldBash() {
        super("Heavy Shield Bash", "Delivers a heavy shield bash that stuns the foe and fortifies your defense. Deals 35 Kinetic damage, slows the enemy (-2 Speed for 2 rounds), and raises Resistance (+2 Defense for 2 rounds). Costs 15 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 35;
    }

    @Override
    public int getManaCost() {
        return 15;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.SLOWNESS, 2, 2)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.RESISTANCE, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
