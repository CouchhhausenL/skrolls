package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class AetherStream extends Spell {
    public AetherStream() {
        super("Aether Stream", "Channels a sustained river of raw aether through the target, restoring your mana and sharpening arcane focus.", MagicType.ARCANE);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(115, this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_RESTORE, 35, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_REGEN, 15, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.FOCUS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
