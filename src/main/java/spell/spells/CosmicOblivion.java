package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class CosmicOblivion extends Spell {
    public CosmicOblivion() {
        super("Cosmic Oblivion", "Unleashes the annihilating collapse of galaxies, erasing enemy boons, breaking defenses, and placing a dreadful curse.", MagicType.INFINITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(160, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 3, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 20, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
