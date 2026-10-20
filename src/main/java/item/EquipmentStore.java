package main.java.item;

import main.java.verb.Verb;
import main.java.verb.VerbSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentStore implements VerbSupplier {
    private final Map<EquipmentType<?>, List<Equippable>> items = new HashMap<>();

    public EquipmentStore() {
        for (EquipmentType<?> type : EquipmentType.values()) {
            items.put(type, new ArrayList<>());
        }
    }

    public boolean equip(Equippable equippable) {
        EquipmentType<?> type = equippable.getEquipmentType();
        if (equippable.getClass() != type.getClassType())
            throw new IllegalArgumentException("Equipable and EquipmentType classes do not match");
        List<Equippable> list = items.get(type);
        if (list.size() == type.getMaxEquipable()) return false;
        list.add(equippable);
        return true;
    }

    public boolean unequip(Equippable equippable) {
        EquipmentType<?> type = equippable.getEquipmentType();
        if (!equippable.getClass().equals(type.getClassType()))
            throw new IllegalArgumentException("Equipable and EquipmentType classes do not match");
        List<Equippable> list = items.get(type);
        return list.remove(equippable);
    }

    public <T extends Equippable> List<T> getEquipables(EquipmentType<T> type) {
        Class<T> clazz = type.getClassType();
        return items.get(type)
                .stream()
                .map(clazz::cast)
                .toList();
    }

    public <T extends Equippable> T getEquipable(EquipmentType<T> type) {
        List<T> list = getEquipables(type);
        if (list.isEmpty()) return null;
        return list.getFirst();
    }

    @Override
    public List<Verb> getVerbs() {
        List<Verb> verbs = new ArrayList<>();

        for (EquipmentType<?> type : EquipmentType.values()) {
            for (Equippable equippable : getEquipables(type)) {
                String name = equippable.getName();

                Verb.Builder builder = Verb.builder("unequip")
                        .aliases("de-equip");
                builder.variant().choice("item", name).build().build();
                verbs.add(builder.build(((executionContext, context) -> {
                    if (context.unequipItem(equippable)) {
                        context.addMessage("Unequipped " + name);
                    } else {
                        context.addMessage("Failed to unequip " + name);
                    }
                })));
            }
        }

        return verbs;
    }
}
