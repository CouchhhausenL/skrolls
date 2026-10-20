package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class TitanGrit extends Spell {
    public TitanGrit() {
        super("Titan Grit", "Channels immense physical fortitude, boosting attack strength and resistance.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 3, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "magic.ska";
    }
}
