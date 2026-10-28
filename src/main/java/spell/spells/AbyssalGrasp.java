package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class AbyssalGrasp extends Spell {
    public AbyssalGrasp() {
        super("Abyssal Grasp", "Summons shadowy tendrils from the abyss to drag the foe down. Deals 75 Abyssal damage, slows enemy Speed (-3 for 3 rounds), and reduces Attack (-2 for 3 rounds). Costs 55 Mana.", MagicType.ABYSSAL);
    }

    @Override
    public int getBaseDamage() {
        return 75;
    }

    @Override
    public int getManaCost() {
        return 55;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.SLOWNESS, 3, 3),
                new CombatEffect(CombatEffectType.WEAKNESS, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 3, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
