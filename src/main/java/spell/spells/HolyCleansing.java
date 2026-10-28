package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class HolyCleansing extends Spell {
    public HolyCleansing() {
        super("Holy Cleansing", "Radiates pure holy light that harms the foe while purifying you. Deals 40 Purity damage, cleanses all negative stat debuffs, and restores 35 Health. Costs 30 Mana.", MagicType.PURITY);
    }

    @Override
    public int getBaseDamage() {
        return 40;
    }

    @Override
    public int getManaCost() {
        return 30;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.HEALING, 35, 0),
                new CombatEffect(CombatEffectType.CLEANSE, 0, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 35, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CLEANSE, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
