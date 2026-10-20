package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ChainLightning extends Spell {
    public ChainLightning() {
        super("Chain Lightning", "A crackling bolt of electricity that zaps the enemy, boosting your speed while slowing the foe.", MagicType.ELEMENTAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(85, this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.SPEED, 2, 2));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 1, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
