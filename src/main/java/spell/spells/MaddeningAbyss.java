package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class MaddeningAbyss extends Spell {
    public MaddeningAbyss() {
        super("Maddening Abyss", "Exposes the enemy mind to eldritch horrors from the abyss, inflicting severe confusion and weakness.", MagicType.ABYSSAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(85, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.5, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
