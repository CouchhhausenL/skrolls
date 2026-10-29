package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.screen.impl.TeleportScreen;
import main.java.spell.Spell;

public class Teleport extends Spell {
    public Teleport() {
        super("Teleport", "Allows you to safely flee from battles.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            context.fleeFromBattle();
        }else {
            context.setScreen(new TeleportScreen());
        }
    }

    @Override
    public boolean usableOutside() {
        return true;
    }

    @Override
    public String getAnimationName() {
        return "attack_buff.ska";
    }
}
