package main.java.verb;

import java.util.List;

public class IntegerVerbParameter extends VerbParameter<Integer> {
    private final int min;
    private final int max;

    private IntegerVerbParameter(String name, Integer defaultValue, int min, int max) {
        super(name, defaultValue);

        if (defaultValue == null)
            throw new IllegalArgumentException("Default value cannot be null");
        if (!(defaultValue >= min && defaultValue <= max))
            throw new IllegalArgumentException("Invalid default value " + defaultValue);

        this.min = min;
        this.max = max;
    }

    @Override
    public VerbMatchResult validate(String input) {
        try {
            int value = Integer.parseInt(input);
            return value >= min && value <= max
                    ? VerbMatchResult.MATCH
                    : VerbMatchResult.NO_MATCH;
        } catch (NumberFormatException ignored) {
            return VerbMatchResult.NO_MATCH;
        }
    }

    @Override
    public List<String> suggest(String prefix) {
        if (defaultValue != null && String.valueOf(defaultValue).startsWith(prefix))
            return List.of(String.valueOf(defaultValue));
        return List.of();
    }

    public static class Builder {
        private final VerbParameterVariant.Builder parent;
        private final String name;
        private final Integer min;
        private final Integer max;
        private Integer defaultValue = null;

        public Builder(VerbParameterVariant.Builder parent, String name, int min, int max) {
            this.parent = parent;
            this.name = name;
            this.min = min;
            this.max = max;
        }

        public Builder defaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public VerbParameterVariant.Builder build() {
            return parent.parameter(new IntegerVerbParameter(name, min, max, defaultValue));
        }

    }
}
