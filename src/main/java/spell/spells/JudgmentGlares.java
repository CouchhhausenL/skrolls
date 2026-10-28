package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class JudgmentGlares extends Spell {
    public JudgmentGlares() {
        super("Judgment Glares", "Pierces the target with harsh divine judgment glares. Deals 85 Purity damage and dispels all enemy periodic buffs. Costs 40 Mana.", MagicType.PURITY);
    }

    @Override
    public int getBaseDamage() {
        return 85;
    }

    @Override
    public int getManaCost() {
        return 40;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.DISPEL, 0, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
