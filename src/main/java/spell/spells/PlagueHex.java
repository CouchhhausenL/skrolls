package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class PlagueHex extends Spell {
    public PlagueHex() {
        super("Plague Hex", "Infects the enemy with a rotting pestilence that worsens each turn and enervates their strikes.", MagicType.NECROTIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(65, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.POISONED, 15, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
