package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class HeavyShieldBash extends Spell {
    public HeavyShieldBash() {
        super("Heavy Shield Bash", "A blunt concussive strike that stuns the target and fortifies your defense.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(35, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
