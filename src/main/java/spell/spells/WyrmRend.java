package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class WyrmRend extends Spell {
    public WyrmRend() {
        super("Wyrm Rend", "Rips into the foe with ancient draconic talons. Deals 125 Dragonic damage, causes deep Bleeding wounds (25% damage/round for 3 rounds), and grants Critical Focus (Level 3 for 2 rounds). Costs 95 Mana.", MagicType.DRAGONIC);
    }

    @Override
    public int getBaseDamage() {
        return 125;
    }

    @Override
    public int getManaCost() {
        return 95;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.BLEEDING, 25, 3)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 25, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 3, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
