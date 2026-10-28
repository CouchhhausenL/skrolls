package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.spell.Spell;

public class Fireball extends Spell {
    public Fireball() {
        super("Fireball", "Hurls an explosive sphere of concentrated fire at the enemy. Deals 90 Elemental damage. Costs 45 Mana.", MagicType.ELEMENTAL);
    }

    @Override
    public int getBaseDamage() {
        return 90;
    }

    @Override
    public int getManaCost() {
        return 45;
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
        return "attack_ball.ska";
    }
}
