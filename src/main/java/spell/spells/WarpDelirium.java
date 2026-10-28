package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class WarpDelirium extends Spell {
    public WarpDelirium() {
        super("Warp Delirium", "Warps reality into fevered chaotic hallucinations. Cleanses negative stat debuffs and grants Critical Focus (Level 4 for 3 rounds), while throwing the enemy into deep Confusion (70% self-harm chance for 4 rounds) and Slowness (-3 Speed for 3 rounds). Costs 110 Mana.", MagicType.CHAOS);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 110;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CONFUSED, 0.7, 4),
                new CombatEffect(CombatEffectType.SLOWNESS, 3, 3)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 4, 3),
                new CombatEffect(CombatEffectType.CLEANSE, 0, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.7, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 3, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 4, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CLEANSE, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buble.ska";
    }
}
