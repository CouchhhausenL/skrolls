package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class InfernoBurst extends Spell {
    public InfernoBurst() {
        super("Inferno Burst", "Engulfs the target in a raging burst of scorching flames. Deals 80 Elemental damage and inflicts Burns (15% damage/round for 3 rounds). Costs 45 Mana.", MagicType.ELEMENTAL);
    }

    @Override
    public int getBaseDamage() {
        return 80;
    }

    @Override
    public int getManaCost() {
        return 45;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.BURNED, 15, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 15, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
