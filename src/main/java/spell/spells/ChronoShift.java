package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class ChronoShift extends Spell {
    public ChronoShift() {
        super("Chrono Shift", "Distorts the flow of time to rob the target of momentum while accelerating yourself. Deals 110 Arcane damage, slows the enemy (-3 Speed for 3 rounds), boosts your Speed (+4 for 3 rounds), and grants Critical Focus (Level 3 for 3 rounds). Costs 85 Mana.", MagicType.ARCANE);
    }

    @Override
    public int getBaseDamage() {
        return 110;
    }

    @Override
    public int getManaCost() {
        return 85;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.SLOWNESS, 3, 3)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.SPEED, 4, 3),
                new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.SPEED, 4, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 3, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
