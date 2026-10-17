package main.java.verb;

import java.util.List;

public abstract class VerbParameter<T> {
    private final String name; // Can be null
    protected final T defaultValue;

    protected VerbParameter(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public abstract VerbParameterMatchResult validate(String input);

    public abstract List<String> suggest(String prefix);
}
