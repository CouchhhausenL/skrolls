package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ThunderSpark extends Spell {
    public ThunderSpark() {
        super("Thunder Spark", "A lightning bolt that surges through you, heightening critical focus.", MagicType.ELEMENTAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(70, this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
