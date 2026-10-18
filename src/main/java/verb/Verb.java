package main.java.verb;

import java.util.*;

public class Verb {
    private final String name;
    private final List<String> aliases;
    private final List<VerbVariant> parameters;

    private Verb(String name, List<String> aliases, List<VerbVariant> parameters) {
        Objects.requireNonNull(name, "Name cannot be null");
        this.name = name;
        this.aliases = aliases;
        this.parameters = parameters;
    }

    public boolean matchesNameOrAlias(String input) {
        if (name.equalsIgnoreCase(input)) return true;

        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(input)) return true;
        }

        return false;
    }

    public List<VerbVariant> getVariants() {
        return Collections.unmodifiableList(parameters);
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final String name;
        private final List<String> aliases = new ArrayList<>();
        private final List<VerbVariant> parameters = new ArrayList<>();

        private Builder(String name) {
            this.name = name;
        }

        public Builder aliases(String... aliases) {
            this.aliases.addAll(Arrays.asList(aliases));
            return this;
        }

        public Builder variant(VerbVariant parameters) {
            this.parameters.add(parameters);
            return this;
        }

        public VerbVariant.Builder variant() {
            return new VerbVariant.Builder(this);
        }

        public Verb build() {
            return new Verb(name, aliases, parameters);
        }

    }
}
