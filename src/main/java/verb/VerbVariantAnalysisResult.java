package main.java.verb;

import java.util.List;

public record VerbVariantAnalysisResult(
        Type type,
        VerbVariant verbVariant,
        List<VerbParameterAnalysisResult> parameterResults
) {

    public boolean isFullyValid() {
        return type.getValidity() == Validity.FULL;
    }

    public boolean isPartiallyValid() {
        return type.getValidity() == Validity.PARTIAL;
    }

    public boolean isInvalid() {
        return type.getValidity() == Validity.NONE;
    }

    public int getConsumedWordCount() {
        return parameterResults.getLast().endWord() + 1;
    }

    public enum Type {
        FULL(Validity.FULL),
        FULL_DEFAULT(Validity.FULL),

        PARTIAL_MISSING_PARAMETERS(Validity.PARTIAL),
        PARTIAL_INCOMPLETE_PARAMETER(Validity.PARTIAL),

        NONE_TOO_MANY_WORDS(Validity.NONE),
        NONE_NO_MATCH(Validity.NONE);

        private final Validity validity;

        Type(Validity validity) {
            this.validity = validity;
        }

        public Validity getValidity() {
            return validity;
        }
    }

    public enum Validity {
        FULL,
        PARTIAL,
        NONE
    }
}
