package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.spell.Spell;

public class ShockwaveLance extends Spell {
    public ShockwaveLance() {
        super("Shockwave Lance", "Condenses kinetic shock into a piercing frontal thrust. Deals 60 Kinetic damage. Costs 20 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 60;
    }

    @Override
    public int getManaCost() {
        return 20;
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
