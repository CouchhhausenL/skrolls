package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class WyrmRend extends Spell {
    public WyrmRend() {
        super("Wyrm Rend", "Rips into the foe with ancient draconic talons, inflicting severe bleeding wounds and sharpening critical focus.", MagicType.DRAGONIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(125, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 25, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
