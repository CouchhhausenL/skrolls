package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class Deathbloom extends Spell {
    public Deathbloom() {
        super("Deathbloom", "Deals necrotic damage and steals a bit of Life", MagicType.NECROTIC);
    }
    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            int damage = battleContext.damageEnemy(60, MagicType.NECROTIC);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, damage * 0.1, 0));

        }
    }
    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
