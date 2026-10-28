package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class LightRay extends Spell {
    public LightRay() {
        super("Light Ray", "Fires a concentrated beam of holy radiance that sears the enemy and knits your wounds. Deals 70 Purity damage and restores 30 Health. Costs 35 Mana.", MagicType.PURITY);
    }

    @Override
    public int getBaseDamage() {
        return 70;
    }

    @Override
    public int getManaCost() {
        return 35;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.HEALING, 30, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 30, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
