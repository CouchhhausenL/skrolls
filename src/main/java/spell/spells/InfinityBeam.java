package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class InfinityBeam extends Spell {
    public InfinityBeam() {
        super("Infinity Beam", "Fires a concentrated ray of pure infinite destructive power across dimensions. Deals 175 Infinity damage, causes deep Bleeding (25% damage/round for 3 rounds), and inflicts cosmic Burns (25% damage/round for 3 rounds). Costs 250 Mana.", MagicType.INFINITY);
    }

    @Override
    public int getBaseDamage() {
        return 175;
    }

    @Override
    public int getManaCost() {
        return 250;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.BLEEDING, 25, 3),
                new CombatEffect(CombatEffectType.BURNED, 25, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BLEEDING, 25, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 25, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_bolt.ska";
    }
}
