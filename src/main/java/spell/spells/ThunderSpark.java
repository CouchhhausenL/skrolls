package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class ThunderSpark extends Spell {
    public ThunderSpark() {
        super("Thunder Spark", "Discharges a sudden bolt of electrical energy that jolts the enemy and heightens your focus. Deals 70 Elemental damage and grants Critical Focus (Level 2 for 2 rounds). Costs 30 Mana.", MagicType.ELEMENTAL);
    }

    @Override
    public int getBaseDamage() {
        return 70;
    }

    @Override
    public int getManaCost() {
        return 30;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
