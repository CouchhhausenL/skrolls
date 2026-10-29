package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.spell.Spell;

public class HardShove extends Spell {
    public HardShove() {
        super("Hard Shove", "A blunt burst of physical force that shoves the target backward. Deals 40 Kinetic damage. Costs 10 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 40;
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
