package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class FlurryOfBlows extends Spell {
    public FlurryOfBlows() {
        super("Flurry of Blows", "Delivers rapid physical strikes that rip into enemy flesh. Deals 45 Kinetic damage and inflicts Bleeding (10% damage/round for 3 rounds). Costs 10 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 45;
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.BLEEDING, 10, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 10, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
