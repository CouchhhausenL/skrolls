package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class FrostNova extends Spell {
    public FrostNova() {
        super("Frost Nova", "Unleashes an icy blast that freezes the enemy, drastically reducing their speed.", MagicType.ELEMENTAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(65, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.FROZEN, 0, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
