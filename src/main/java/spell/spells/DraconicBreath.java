package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class DraconicBreath extends Spell {
    public DraconicBreath() {
        super("Draconic Breath", "Unleashes an incinerating blast of primordial dragon flame, setting the target ablaze and scorching armor.", MagicType.DRAGONIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(120, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 25, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
