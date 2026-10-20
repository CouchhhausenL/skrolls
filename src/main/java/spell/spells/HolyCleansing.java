package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class HolyCleansing extends Spell {
    public HolyCleansing() {
        super("Holy Cleansing", "Radiates pure light that purges stat debuffs and restores health.", MagicType.PURITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(40, this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 35, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CLEANSE, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
