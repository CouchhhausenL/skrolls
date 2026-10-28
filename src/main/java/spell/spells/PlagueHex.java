package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class PlagueHex extends Spell {
    public PlagueHex() {
        super("Plague Hex", "Infects the enemy with a rotting necromantic pestilence. Deals 65 Necrotic damage, poisons the enemy (15% worsening damage/round for 4 rounds), and reduces their Attack (-2 for 3 rounds). Costs 50 Mana.", MagicType.NECROTIC);
    }

    @Override
    public int getBaseDamage() {
        return 65;
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.POISONED, 15, 4),
                new CombatEffect(CombatEffectType.WEAKNESS, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.POISONED, 15, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
