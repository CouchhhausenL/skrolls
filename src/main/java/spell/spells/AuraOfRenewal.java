package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class AuraOfRenewal extends Spell {
    public AuraOfRenewal() {
        super("Aura of Renewal", "Bathes you in soothing celestial light. Restores 20 Health immediately and grants Regeneration (15 HP/round for 3 rounds). Costs 25 Mana.", MagicType.PURITY);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 25;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.HEALING, 20, 0),
                new CombatEffect(CombatEffectType.REGENERATION, 15, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 20, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.REGENERATION, 15, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buff.ska";
    }
}
