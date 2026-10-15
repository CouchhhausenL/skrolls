package main.java.verb;

import java.util.List;

public class ChoiceVerbParameter extends VerbParameter<String> {
    private final List<String> options;

    private ChoiceVerbParameter(String name, String defaultValue, List<String> options) {
        super(name, defaultValue);
        this.options = options;
    }

    @Override
    public VerbMatchResult validate(String input) {
        String lowerCaseInput = input.toLowerCase();

        VerbMatchResult result = VerbMatchResult.NO_MATCH;
        for (String choice : options) {
            String lowerCaseChoice = choice.toLowerCase();
            if (lowerCaseChoice.equals(lowerCaseInput)) return VerbMatchResult.MATCH;
            if (lowerCaseChoice.startsWith(lowerCaseInput)) return VerbMatchResult.PARTIAL;
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
        private final VerbParameterVariant.Builder parent;
        private final String name;
        private final List<String> choices;
        private String defaultValue;

        public Builder(VerbParameterVariant.Builder parent, String name, String[] choices) {
            this.parent = parent;
            this.name = name;
            this.choices = List.of(choices);
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public VerbParameterVariant.Builder build() {
            parent.parameter(new ChoiceVerbParameter(name, defaultValue, choices));
            return parent;
        }
    }
}
