package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class DragonRoar extends Spell {
    public DragonRoar() {
        super("Dragon Roar", "A deafening roar of draconic dominion that cowers the foe with weakness and slowness while bolstering your own strength.", MagicType.DRAGONIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(105, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 3, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 3, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
