package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class RadiantSmite extends Spell {
    public RadiantSmite() {
        super("Radiant Smite", "Strikes the enemy with holy light, healing yourself, weakening the foe, and dispelling dark status effects.", MagicType.PURITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(80, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 20, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
