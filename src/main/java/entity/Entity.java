package main.java.entity;

import main.java.MagicType;
import main.java.entity.player.Player;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.item.items.Potion;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


// Shared combatant state for players and monsters.
// Stat stages use the -6..+6 scale and are modified independently of the
// base stats; combat effects may additionally shift the effective stage.
public abstract class Entity {
    private final String name;
    private int health;
    private int maxHealth;
    private final int speed;
    private final Map<Stat, Integer> statStages = new EnumMap<>(Stat.class);
    private final List<CombatEffect> combatEffects = new ArrayList<>();


    public Entity(String name, int maxHealth, int speed) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.speed = speed;
        for (Stat stat : Stat.values()) {
            statStages.put(stat, 0);
        }
    }

    // Returns true while the entity is still alive (health > 0)
    public boolean takeDamage(int amount) {
        health = health - amount;

        return (health > 0);
    }

    // Percentage is relative to max health, not current health
    public boolean takePercentualDamage(double percentage) {
        health = (int) (health - (maxHealth * percentage / 100));

        return (health > 0);

    }

    public void heal(int amount) {
        if (amount < 0) {
            return;
        }

        health += amount;

        if (health > getMaxHealth()) {
            health = getMaxHealth();
        }
    }

    // Note: percentage is a fraction here (e.g. 0.05 = 5% of max health)
    public void healPercentage(double percentage) {
        health = (int) Math.floor(health + (maxHealth * percentage));
    }

    // Permanently reduces max health by a percentage of the current max
    public void maxHealthPercentageDrain(double percent) {
        maxHealth = (int) Math.min(0, Math.floor(maxHealth - maxHealth * percent / 100));
    }


    // Effective stage = stored stage + buff/debuff effect strengths,
    // clamped to the legal -6..+6 range
    public int getStatStage(Stat stat) {
        int baseStage = statStages.getOrDefault(stat, 0);
        baseStage = baseStage + switch (stat) {
            case ATTACK ->
                    getCombinedCombatEffect(CombatEffectType.STRENGTH) + getCombinedCombatEffect(CombatEffectType.WEAKNESS);
            case DEFENSE ->
                    getCombinedCombatEffect(CombatEffectType.RESISTANCE) + getCombinedCombatEffect(CombatEffectType.Vulnerability);
            case SPEED ->
                    getCombinedCombatEffect(CombatEffectType.SPEED) + getCombinedCombatEffect(CombatEffectType.SLOWNESS);
        };
        baseStage = Math.clamp(baseStage, -6, 6);
        return baseStage;
    }

    // Duration 0 means instant: apply once, then remove the effect.
    // Periodic effects stay in the list and are handled by applyPeriodicEffects().
    public void applyInstantEffects() {
        List<CombatEffect> instantEffects = combatEffects.stream()
                .filter(effect -> effect.getDuration() == 0)
                .toList();

        for (CombatEffect instantEffect : instantEffects) {
            double effect = instantEffect.getEffectStrength();

            switch (instantEffect.getCombatEffectType()) {
                case HEALING -> heal((int) effect);
                case MANA_RESTORE -> {
                    if (this instanceof Player player) {
                        player.gainMana((int) effect);
                    }
                }
                case DAMAGE -> takeDamage((int) effect);
                case DISPEL ->
                        combatEffects.removeIf(specificEffect -> specificEffect.getCombatEffectType().isPeriodic());
            }

            combatEffects.remove(instantEffect);
        }
    }


    public void modifyStatStage(Stat stat, int amount) {
        int current = getStatStage(stat);
        int updated = Math.clamp(current + amount, -6, 6);
        statStages.put(stat, updated);
    }

    public void setStatStage(Stat stat, int stage) {
        int updated = Math.clamp(stage, -6, 6);
        statStages.put(stat, updated);
    }


    public void resetStatStages() {
        for (Stat stat : Stat.values()) {
            statStages.put(stat, 0);
        }
    }


    public void resetStatStage(Stat stat) {
        statStages.put(stat, 0);
    }

    public void resetCombatEffects() {
        combatEffects.clear();
    }

    public void drinkPotion(Potion potion) {
        for (CombatEffect combatEffect : potion.combatEffects) {
            addCombatEffect(combatEffect);
        }

    }

    // Instant effects are consumed immediately; periodic ones remain active
    public void addCombatEffect(CombatEffect combatEffect) {
        combatEffects.add(combatEffect);
        applyInstantEffects();
    }

    public void removeCombatEffect(CombatEffect combatEffect) {
        combatEffects.remove(combatEffect);
    }

    // tickDuration() returns true when the effect expired
    public void tickAllCombatEffects() {
        combatEffects.removeIf(CombatEffect::tickDuration);
    }

    public int getCombinedCombatEffect(CombatEffectType combatEffectType) {
        int combined = 0;
        for (CombatEffect combatEffect : combatEffects) {
            if (combatEffect.getCombatEffectType() == combatEffectType) {
                combined = (int) (combined + combatEffect.getEffectStrength());
            }
        }
        return combined;
    }


    // Applies all active periodic effects once per round.
    // Damage is percentage-based (relative to max health); burn weakens over
    // time while poison worsens. Cursed permanently drains maximum pools.
    public void applyPeriodicEffects() {
        for (CombatEffect combatEffect : combatEffects) {
            CombatEffectType combatEffectType = combatEffect.getCombatEffectType();
            if (combatEffectType.isPeriodic()) {
                double effectStrength = combatEffect.getEffectStrength();
                switch (combatEffectType) {
                    case BURNED:
                        takePercentualDamage(Math.max(1, effectStrength - combatEffect.getCurrentRound()));
                        break;
                    case POISONED:
                        takePercentualDamage(effectStrength + combatEffect.getCurrentRound());
                        break;
                    case CONFUSED:
                        // Random chance to hurt itself
                        if (Math.random() > 0.5) {
                            takePercentualDamage(effectStrength);
                        }
                        break;
                    case BLEEDING:
                        takePercentualDamage(effectStrength);
                        break;
                    case CURSED:
                        maxHealthPercentageDrain(effectStrength);
                        if (this instanceof Player) {
                            ((Player) this).maxManaPercentageDrain(effectStrength);
                        }
                        break;

                    case REGENERATION:
                        // Regeneration always heals a fixed 5% of max health
                        healPercentage(5);
                        break;
                    case MANA_REGEN:
                        if (this instanceof Player) {
                            ((Player) this).regenerateMana(effectStrength);
                        }
                        break;


                }

            }
        }
    }


    public Boolean isAlive() {
        return health > 0;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public abstract int getDefenseStat();

    public abstract MagicType getDefendingMagicType();

    public abstract MagicType getAttackingMagicType();

    public abstract int getLevel();

    public abstract int getAttackStat();


    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
