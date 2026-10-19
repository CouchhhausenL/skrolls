package main.java.verb;

import main.java.game.GameContext;

import java.util.*;

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

    public boolean allVariantsHaveParameterName(String name) {
        for (VerbVariant variant : variants) {
            if (!variant.hasParameterWithName(name))
                return false;
        }
        return true;
    }

    public List<VerbVariantAnalysisResult> analyze(String input) {
        List<VerbVariantAnalysisResult> results = new ArrayList<>();
        for (VerbVariant variant : variants) {
            results.addAll(variant.analyze(input));
        }
        return results;
    }

    public void execute(VerbVariantAnalysisResult result, GameContext gameContext) {
        VerbExecutionContext executionContext = new VerbExecutionContext(this, result);
        onExecute.execute(executionContext, gameContext);
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

        public Verb build(OnExecute onExecute) {
            return new Verb(name, aliases, parameters, onExecute);
        }

    }

    public interface OnExecute {
        void execute(VerbExecutionContext executionContext, GameContext context);
    }
}
