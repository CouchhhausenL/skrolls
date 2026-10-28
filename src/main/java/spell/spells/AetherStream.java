package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class AetherStream extends Spell {
    public AetherStream() {
        super("Aether Stream", "Channels a sustained river of raw aether through the target, recycling energy back into you. Deals 115 Arcane damage, restores 35 Mana instantly, grants Mana Regeneration (+15/round for 3 rounds), and increases Focus (+2 for 3 rounds). Costs 80 Mana.", MagicType.ARCANE);
    }

    @Override
    public int getBaseDamage() {
        return 115;
    }

    @Override
    public int getManaCost() {
        return 80;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.MANA_RESTORE, 35, 0),
                new CombatEffect(CombatEffectType.MANA_REGEN, 15, 3),
                new CombatEffect(CombatEffectType.FOCUS, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
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
