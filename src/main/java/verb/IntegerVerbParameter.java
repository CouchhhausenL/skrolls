package main.java.verb;

import java.util.ArrayList;
import java.util.List;

public class IntegerVerbParameter extends VerbParameter<Integer> {
    private final int min;
    private final int max;

    private IntegerVerbParameter(String name, Integer defaultValue, int min, int max) {
        super(name, defaultValue);

        if (defaultValue != null)
            if (!(defaultValue >= min && defaultValue <= max))
                throw new IllegalArgumentException(String.format(
                        "defaultValue is invalid (%d is not in range %d to %d)",
                        defaultValue, min, max
                ));

        this.min = min;
        this.max = max;
    }

    @Override
    public Integer getValue(String input) {
        try {
            int value = Integer.parseInt(input);
            if (value >= min && value <= max)
                return value;
        } catch (NumberFormatException ignored) {
        }

        throw new IllegalArgumentException("input is invalid");
    }

    @Override
    public VerbParameterMatchResult validate(String input) {
        if (input.isEmpty())
            return VerbParameterMatchResult.PARTIAL;
        try {
            int value = Integer.parseInt(input);
            return value >= min && value <= max
                    ? VerbParameterMatchResult.MATCH
                    : VerbParameterMatchResult.NO_MATCH;
        } catch (NumberFormatException ignored) {
            return VerbParameterMatchResult.NO_MATCH;
        }
    }

    @Override
    public List<String> suggest(String prefix) {
        List<String> list = new ArrayList<>();
        for (int i = min; i < max; i++) {
            String str = String.valueOf(i);
            if (str.startsWith(prefix))
                list.add(str);
        }
        return list;
    }

    public static class Builder {
        private final VerbVariant.Builder parent;
        private final String name;
        private final Integer min;
        private final Integer max;
        private Integer defaultValue = null;

        public Builder(VerbVariant.Builder parent, String name, int min, int max) {
            this.parent = parent;
            this.name = name;
            this.min = min;
            this.max = max;
        }

        public Builder defaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public VerbVariant.Builder build() {
            return parent.parameter(new IntegerVerbParameter(name, defaultValue, min, max));
        }

    }
}
