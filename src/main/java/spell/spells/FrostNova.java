package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class FrostNova extends Spell {
    public FrostNova() {
        super("Frost Nova", "Unleashes an icy blizzard that chills the enemy to the bone. Deals 65 Elemental damage and freezes the target (halves speed for 2 rounds). Costs 35 Mana.", MagicType.ELEMENTAL);
    }

    @Override
    public int getBaseDamage() {
        return 65;
    }

    @Override
    public int getManaCost() {
        return 35;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.FROZEN, 0, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.FROZEN, 0, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
