package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class DoomGrave extends Spell {
    public DoomGrave() {
        super("Doom Grave", "Beckons the eternal cold of the grave, burdening the enemy with a creeping curse and slowing their actions.", MagicType.NECROTIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(90, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 15, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
