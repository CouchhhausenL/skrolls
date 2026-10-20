package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class EntropyOverload extends Spell {
    public EntropyOverload() {
        super("Entropy Overload", "Discharges erratic entropic energy, stripping enemy buffs, weakening their strikes, and supercharging your attack strength and speed.", MagicType.CHAOS);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(140, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 3, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 4, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.SPEED, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
