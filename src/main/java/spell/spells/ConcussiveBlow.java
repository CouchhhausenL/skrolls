package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ConcussiveBlow extends Spell {
    public ConcussiveBlow() {
        super("Concussive Blow", "Delivers a concussive shock to the enemy's head, leaving them confused and vulnerable.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(50, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.4, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
