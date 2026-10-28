package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class ShadowSiphon extends Spell {
    public ShadowSiphon() {
        super("Shadow Siphon", "Draws dark essence from the enemy, siphoning life and mana back to you. Deals 80 Abyssal damage, heals you for 30% of damage dealt, and restores 25 Mana. Costs 50 Mana.", MagicType.ABYSSAL);
    }

    @Override
    public int getBaseDamage() {
        return 80;
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.HEALING, 24, 0),
                new CombatEffect(CombatEffectType.MANA_RESTORE, 25, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            int damage = battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, damage * 0.3, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_RESTORE, 25, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buble.ska";
    }
}
