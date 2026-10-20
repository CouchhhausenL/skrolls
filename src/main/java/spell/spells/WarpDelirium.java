package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class WarpDelirium extends Spell {
    public WarpDelirium() {
        super("Warp Delirium", "Warps reality into fevered illusions, severely confusing and slowing the foe while cleansing your status and honing critical instinct.", MagicType.CHAOS);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.7, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 3, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 4, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CLEANSE, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buble.ska";
    }
}
