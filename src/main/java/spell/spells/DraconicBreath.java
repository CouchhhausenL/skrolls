package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class DraconicBreath extends Spell {
    public DraconicBreath() {
        super("Draconic Breath", "Exhales an incinerating torrent of primordial dragon flame. Deals 120 Dragonic damage, inflicts severe Burns (25% damage/round for 3 rounds), and leaves the target Vulnerable (+2 Defense reduction for 2 rounds). Costs 90 Mana.", MagicType.DRAGONIC);
    }

    @Override
    public int getBaseDamage() {
        return 120;
    }

    @Override
    public int getManaCost() {
        return 90;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.BURNED, 25, 3),
                new CombatEffect(CombatEffectType.Vulnerability, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.BURNED, 25, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_ball.ska";
    }
}
