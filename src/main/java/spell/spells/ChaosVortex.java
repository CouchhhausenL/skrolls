package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class ChaosVortex extends Spell {
    public ChaosVortex() {
        super("Chaos Vortex", "Rips open a swirling vortex of turbulent entropy. Deals 135 Chaos damage, burns the foe (20% damage/round for 3 rounds), causes Confusion (50% self-hit chance for 3 rounds), and leaves them Vulnerable (+2 Defense reduction for 2 rounds). Costs 140 Mana.", MagicType.CHAOS);
    }

    @Override
    public int getBaseDamage() {
        return 135;
    }

    @Override
    public int getManaCost() {
        return 140;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CONFUSED, 0.5, 3),
                new CombatEffect(CombatEffectType.BURNED, 20, 3),
                new CombatEffect(CombatEffectType.Vulnerability, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
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
