package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class DragonRoar extends Spell {
    public DragonRoar() {
        super("Dragon Roar", "A deafening roar of draconic dominion that cowers the enemy and empowers the caster. Deals 105 Dragonic damage, weakens enemy Attack (-3 for 3 rounds), slows enemy Speed (-2 for 2 rounds), and boosts your Attack (+3 for 3 rounds). Costs 80 Mana.", MagicType.DRAGONIC);
    }

    @Override
    public int getBaseDamage() {
        return 105;
    }

    @Override
    public int getManaCost() {
        return 80;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.WEAKNESS, 3, 3),
                new CombatEffect(CombatEffectType.SLOWNESS, 2, 2)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.STRENGTH, 3, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 3, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 2));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 3, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
