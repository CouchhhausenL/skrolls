package main.java.verb;

import java.util.List;

// Parameter limited to a fixed set of string options; matching is case-insensitive
public class ChoiceVerbParameter extends VerbParameter<String> {
    private final List<String> options;

    private ChoiceVerbParameter(String name, String defaultValue, List<String> options) {
        super(name, defaultValue);
        this.options = options;
    }

    @Override
    public String getValue(String input) {
        String normalizedInput = input.toLowerCase();
        for (String option : options) {
            String normalizedOption = option.toLowerCase();
            if (normalizedOption.equals(normalizedInput))
                return normalizedOption;
        }

        throw new IllegalArgumentException("Input is invalid");
    }

    // Classifies the input against the option list:
    // MATCH            -> exactly one option equals it
    // PARTIAL          -> it is a prefix of at least one option (may extend)
    // MATCH_AND_PARTIAL-> both cases apply, so parsing must continue
    @Override
    public VerbParameterMatchResult validate(String input) {
        String normalizedInput = input.toLowerCase();

        VerbParameterMatchResult result = VerbParameterMatchResult.NO_MATCH;
        for (String option : options) {
            String normalizedOption = option.toLowerCase();
            if (normalizedOption.equals(normalizedInput)) {
                // Full match upgrades an earlier PARTIAL to MATCH_AND_PARTIAL
                result = result == VerbParameterMatchResult.PARTIAL
                        ? VerbParameterMatchResult.MATCH_AND_PARTIAL
                        : VerbParameterMatchResult.MATCH;
            } else if (normalizedOption.startsWith(normalizedInput)) {
                // Prefix match upgrades an earlier MATCH to MATCH_AND_PARTIAL
                result = result == VerbParameterMatchResult.MATCH
                        ? VerbParameterMatchResult.MATCH_AND_PARTIAL
                        : VerbParameterMatchResult.PARTIAL;
            }

            // Both outcomes are possible, no need to keep scanning
            if (result == VerbParameterMatchResult.MATCH_AND_PARTIAL) return result;
        }
        return result;
    }

    @Override
    public List<String> suggest(String prefix) {
        String normalized = prefix.toLowerCase();

        return options.stream()
                .filter(option ->
                        option.toLowerCase()
                                .startsWith(normalized))
                .toList();
    }

    public static class Builder {
        private final VerbVariant.Builder parent;
        private final String name;
        private final List<String> options;
        private String defaultValue;

        public Builder(VerbVariant.Builder parent, String name, String[] options) {
            this.parent = parent;
            this.name = name;
            this.options = List.of(options);
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public VerbVariant.Builder build() {
            parent.parameter(new ChoiceVerbParameter(name, defaultValue, options));
            return parent;
        }
    }
}
