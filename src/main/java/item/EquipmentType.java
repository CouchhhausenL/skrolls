package main.java.item;

import main.java.item.items.Armor;
import main.java.item.items.Artifact;
import main.java.item.items.Scroll;
import main.java.item.items.Staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EquipmentType<T extends Equippable> {

    private static final List<EquipmentType<?>> VALUES = new ArrayList<>();

    public static final EquipmentType<Armor> ARMOR =
            register(1, Armor.class);

    public static final EquipmentType<Staff> STAFF =
            register(1, Staff.class);

    public static final EquipmentType<Artifact> ARTIFACT =
            register(3, Artifact.class);

    public static final EquipmentType<Scroll> SCROLL =
            register(5, Scroll.class);

    private final int maxEquipable;
    private final Class<T> classType;

    private EquipmentType(int maxEquipable, Class<T> classType) {
        this.maxEquipable = maxEquipable;
        this.classType = classType;
    }

    private static <T extends Equippable> EquipmentType<T> register(
            int maxEquipable, Class<T> classType) {

        EquipmentType<T> type = new EquipmentType<>(maxEquipable, classType);
        VALUES.add(type);
        return type;
    }

    public static List<EquipmentType<?>> values() {
        return Collections.unmodifiableList(VALUES);
    }

    public int getMaxEquipable() {
        return maxEquipable;
    }

    public Class<T> getClassType() {
        return classType;
    }
}
