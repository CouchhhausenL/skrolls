package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class FlurryOfBlows extends Spell {
    public FlurryOfBlows() {
        super("Flurry of Blows", "Rapid physical strikes that tear into the foe, causing bleeding wounds.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(45, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 10, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
