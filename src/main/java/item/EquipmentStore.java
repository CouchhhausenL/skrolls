package main.java.item;

import main.java.verb.Verb;
import main.java.verb.VerbSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentStore implements VerbSupplier {
    private final Map<EquipmentType<?>, List<Equipable>> items = new HashMap<>();

    public EquipmentStore() {
        for (EquipmentType<?> type : EquipmentType.values()) {
            items.put(type, new ArrayList<>());
        }
    }

    public boolean equip(Equipable equipable) {
        EquipmentType<?> type = equipable.getEquipmentType();
        if (equipable.getClass() != type.getClassType())
            throw new IllegalArgumentException("Equipable and EquipmentType classes do not match");
        List<Equipable> list = items.get(type);
        if (list.size() == type.getMaxEquipable()) return false;
        list.add(equipable);
        return true;
    }

    public boolean unequip(Equipable equipable) {
        EquipmentType<?> type = equipable.getEquipmentType();
        if (!equipable.getClass().equals(type.getClassType()))
            throw new IllegalArgumentException("Equipable and EquipmentType classes do not match");
        List<Equipable> list = items.get(type);
        return list.remove(equipable);
    }

    public <T extends Equipable> List<T> getEquipables(EquipmentType<T> type) {
        Class<T> clazz = type.getClassType();
        return items.get(type)
                .stream()
                .map(clazz::cast)
                .toList();
    }

    public <T extends Equipable> T getEquipable(EquipmentType<T> type) {
        List<T> list = getEquipables(type);
        if (list.isEmpty()) return null;
        return list.getFirst();
    }

    @Override
    public List<Verb> getVerbs() {
        List<Verb> verbs = new ArrayList<>();

        for (EquipmentType<?> type : EquipmentType.values()) {
            for (Equipable equipable : getEquipables(type)) {
                Verb.Builder builder = Verb.builder("unequip")
                        .aliases("deequip");
                builder.variant().choice("item", equipable.getName());
                verbs.add(builder.build(((executionContext, context) -> {
                    unequip(equipable);
                    context.getPlayer().getInventory().addItem(equipable);
                })));
            }
        }

        return verbs;
    }
}
