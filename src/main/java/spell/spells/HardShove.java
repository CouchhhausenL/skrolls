package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.spell.Spell;

public class HardShove extends Spell {
    public HardShove() {
        super("Hard Shove", "A blunt burst of force that knocks a target back a few steps.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();

            battleContext.damageEnemy(20, this.magicType);

        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
