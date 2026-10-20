package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ChronoShift extends Spell {
    public ChronoShift() {
        super("Chrono Shift", "Distorts the fabric of time, stealing the enemy's temporal momentum to surge your own speed and critical precision.", MagicType.ARCANE);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(110, this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.SPEED, 4, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 3, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
