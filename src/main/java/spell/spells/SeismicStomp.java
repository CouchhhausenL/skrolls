package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class SeismicStomp extends Spell {
    public SeismicStomp() {
        super("Seismic Stomp", "Slams the ground with immense kinetic force, slowing and weakening the foe.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(40, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
