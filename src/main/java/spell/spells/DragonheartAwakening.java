package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class DragonheartAwakening extends Spell {
    public DragonheartAwakening() {
        super("Dragonheart Awakening", "Awakens the dormant draconic bloodline within, conferring overwhelming strength, resilience, regeneration, and spell focus.", MagicType.DRAGONIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 3, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 3, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.REGENERATION, 20, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.FOCUS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "magic.ska";
    }
}
