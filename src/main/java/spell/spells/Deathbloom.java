package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class Deathbloom extends Spell {
    public Deathbloom() {
        super("Deathbloom", "Conjures a dark necrotic bloom that siphons life force from the target. Deals 60 Necrotic damage and heals you for 10% of the damage dealt. Costs 45 Mana.", MagicType.NECROTIC);
    }

    @Override
    public int getBaseDamage() {
        return 60;
    }

    @Override
    public int getManaCost() {
        return 45;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.HEALING, 6, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            int damage = battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, damage * 0.1, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
