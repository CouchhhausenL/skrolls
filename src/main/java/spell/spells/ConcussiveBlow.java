package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class ConcussiveBlow extends Spell {
    public ConcussiveBlow() {
        super("Concussive Blow", "Delivers a heavy strike to the enemy's head that rattles their senses. Deals 50 Kinetic damage, confuses the target (40% self-hit chance for 3 rounds), and leaves them Vulnerable (+2 Defense reduction for 2 rounds). Costs 15 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 50;
    }

    @Override
    public int getManaCost() {
        return 15;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CONFUSED, 0.4, 3),
                new CombatEffect(CombatEffectType.Vulnerability, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.4, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
