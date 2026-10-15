package main.java.verb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Verb {
    private final String name;
    private final List<String> aliases;
    private final List<VerbParameterVariant> parameters;

    private Verb(String name, List<String> aliases, List<VerbParameterVariant> parameters) {
        Objects.requireNonNull(name, "Name cannot be null");
        this.name = name;
        this.aliases = aliases;
        this.parameters = parameters;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final String name;
        private final List<String> aliases = new ArrayList<>();
        private final List<VerbParameterVariant> parameters = new ArrayList<>();

        private Builder(String name) {
            this.name = name;
        }

        public Builder aliases(String... aliases) {
            this.aliases.addAll(Arrays.asList(aliases));
            return this;
        }

        public Builder variant(VerbParameterVariant parameters) {
            this.parameters.add(parameters);
            return this;
        }

        public VerbParameterVariant.Builder variant() {
            return new VerbParameterVariant.Builder(this);
        }

        public Verb build() {
            return new Verb(name, aliases, parameters);
        }

    }
}
