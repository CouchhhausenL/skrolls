package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.spell.Spell;

public class Fireball extends Spell {
    public Fireball() {
        super("Fireball", "A strong elemental attack dealing high damage amounts.", MagicType.ELEMENTAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();

            battleContext.damageEnemy(90, this.magicType);


        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
