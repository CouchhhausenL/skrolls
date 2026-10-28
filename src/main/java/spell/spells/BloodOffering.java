package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class BloodOffering extends Spell {
    public BloodOffering() {
        super("Blood Offering", "Tears open deep necrotic gashes in the foe to fuel your combat frenzy. Deals 85 Necrotic damage, causes Bleeding (20% damage/round for 3 rounds), grants Critical Focus (Level 3 for 3 rounds), and increases Attack (+2 for 3 rounds). Costs 65 Mana.", MagicType.NECROTIC);
    }

    @Override
    public int getBaseDamage() {
        return 85;
    }

    @Override
    public int getManaCost() {
        return 65;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.BLEEDING, 20, 3)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 3),
                new CombatEffect(CombatEffectType.STRENGTH, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 20, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
