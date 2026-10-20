package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class Rest extends Spell {
    public Rest() {
        super("Rest", "This heals you a little bit", MagicType.KINETIC);
    }
    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();

            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 20, 0));

        }
    }
    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
