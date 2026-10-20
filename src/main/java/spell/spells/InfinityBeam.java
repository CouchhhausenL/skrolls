package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class InfinityBeam extends Spell {
    public InfinityBeam() {
        super("Infinity Beam", "Fires a ray of limitless energy that obliterates all known defense, searing and rending the target across timelines.", MagicType.INFINITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(175, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 25, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 25, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
