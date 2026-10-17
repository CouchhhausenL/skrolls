package main.java.verb;

import java.util.ArrayList;
import java.util.List;

public class VerbParameterVariant {
    private final List<VerbParameter<?>> parameters;

    private VerbParameterVariant(List<VerbParameter<?>> parameters) {
        this.parameters = parameters;
    }

    public void analyze(String input) {
        String[] words = input.split(" ");
        int wordCount = 1;

        for (VerbParameter<?> parameter : parameters) {
            String current = getStringFormWordCount(words, wordCount);
            VerbParameterMatchResult result = parameter.validate(current);
            if (result == VerbParameterMatchResult.NO_MATCH) break;

            wordCount++;
            if (wordCount >= words.length) break;
        }
    }

    private String getStringFormWordCount(String[] words, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(words[i]);
        }
        return result.toString();
    }

    public static class Builder {
        private final Verb.Builder parent;
        private final List<VerbParameter<?>> parameters = new ArrayList<>();

        public Builder(Verb.Builder parent) {
            this.parent = parent;
        }

        public Builder parameter(VerbParameter<?> parameter) {
            parameters.add(parameter);
            return this;
        }

        public ChoiceVerbParameter.Builder choice(String name, String... choices) {
            return new ChoiceVerbParameter.Builder(this, name, choices);
        }

        public IntegerVerbParameter.Builder integer(String name, int min, int max) {
            return new IntegerVerbParameter.Builder(this, name, min, max);
        }

        public Verb.Builder build() {
            return parent.variant(new VerbParameterVariant(parameters));
        }
    }
}
