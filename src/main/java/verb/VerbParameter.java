package main.java.verb;

import java.util.List;

// Base class for a single verb argument.
// name is optional (null = unnamed); defaultValue makes the parameter optional.
public abstract class VerbParameter<T> {
    private final String name; // Can be null
    private final T defaultValue;

    protected VerbParameter(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        return name != null;
    }

    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public abstract T getValue(String input);

    public abstract VerbParameterMatchResult validate(String input);

    public abstract List<String> suggest(String prefix);
}
