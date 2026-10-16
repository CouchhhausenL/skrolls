package main.java.spell.spells;

import main.java.game.GameContext;
import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.spell.Spell;

public class Fireball extends Spell {
    public Fireball(String name, String description, MagicType magicType) {
        super(name, description, magicType);
    }


    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();

            battleContext.damageEnemy(90, MagicType.ELEMENTAL);

        }
    }
}
