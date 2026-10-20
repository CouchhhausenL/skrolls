package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ShatterStrike extends Spell {
    public ShatterStrike() {
        super("Shatter Strike", "A bone-crushing blow that cracks enemy armor, leaving them vulnerable to all damage.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(55, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
