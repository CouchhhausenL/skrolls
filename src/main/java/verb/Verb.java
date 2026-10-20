package main.java.verb;

import main.java.game.GameContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// A command such as "go" or "equip". A verb may have several variants
// (different parameter layouts); the best matching variant is executed.
public class Verb {
    private final String name;
    private final List<String> aliases;
    private final List<VerbVariant> variants;
    private final OnExecute onExecute;

    private Verb(String name, List<String> aliases, List<VerbVariant> variants, OnExecute onExecute) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(onExecute, "onExecute must not be null");
        this.name = name;
        this.aliases = aliases;
        this.variants = variants;
        this.onExecute = onExecute;
    }

    public boolean matchesNameOrAlias(String input) {
        if (name.equalsIgnoreCase(input)) return true;

        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(input)) return true;
        }

        return false;
    }

    public List<String> getAllPossibleNames() {
        List<String> result = new ArrayList<>();
        result.add(name);
        result.addAll(aliases);
        return result;
    }

    public boolean allVariantsHaveParameterName(String name) {
        for (VerbVariant variant : variants) {
            if (!variant.hasParameterWithName(name))
                return false;
        }
        return true;
    }

    public List<String> suggest(String prefix) {
        String normalized = prefix.toLowerCase();

        return getAllPossibleNames().stream()
                .filter(option ->
                        option.toLowerCase()
                                .startsWith(normalized))
                .toList();
    }

    // Analyzes the input against every variant and merges the results
    public List<VerbVariantAnalysisResult> analyze(String input) {
        List<VerbVariantAnalysisResult> results = new ArrayList<>();
        for (VerbVariant variant : variants) {
            results.addAll(variant.analyze(input));
        }
        return results;
    }

    public boolean hasNoVariants() {
        return variants.isEmpty();
    }

    public void execute(VerbVariantAnalysisResult result, GameContext gameContext) {
        VerbExecutionContext executionContext = new VerbExecutionContext(this, result);
        onExecute.execute(executionContext, gameContext);
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    // Fluent builder: builder("go").aliases(...).variant()...build().build(...)
    // where the final build takes the execution callback
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

        public Verb build(OnExecute onExecute) {
            return new Verb(name, aliases, parameters, onExecute);
        }

    }

    public interface OnExecute {
        void execute(VerbExecutionContext executionContext, GameContext context);
    }
}
