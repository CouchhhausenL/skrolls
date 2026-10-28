package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class MoltenSurge extends Spell {
    public MoltenSurge() {
        super("Molten Surge", "Surges superheated magma beneath the enemy's feet. Deals 95 Elemental damage, burns the enemy (20% damage/round for 3 rounds), and leaves them Vulnerable (+1 Defense reduction for 2 rounds). Costs 50 Mana.", MagicType.ELEMENTAL);
    }

    @Override
    public int getBaseDamage() {
        return 95;
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.BURNED, 20, 3),
                new CombatEffect(CombatEffectType.Vulnerability, 1, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 20, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 1, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
