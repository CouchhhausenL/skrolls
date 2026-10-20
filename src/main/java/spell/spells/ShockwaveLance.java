package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.spell.Spell;

public class ShockwaveLance extends Spell {
    public ShockwaveLance() {
        super("Shockwave Lance", "Condenses force into a piercing line that punches through multiple targets.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();

            battleContext.damageEnemy(60, this.magicType);

        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
