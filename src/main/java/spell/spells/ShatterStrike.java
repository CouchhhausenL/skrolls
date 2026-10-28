package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class ShatterStrike extends Spell {
    public ShatterStrike() {
        super("Shatter Strike", "A bone-crushing strike aimed at armor seams. Deals 55 Kinetic damage and inflicts Vulnerability (+2 Defense reduction for 3 rounds). Costs 20 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 55;
    }

    @Override
    public int getManaCost() {
        return 20;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.Vulnerability, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
