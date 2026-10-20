package main.java.verb;

import java.util.List;

// Outcome of analyzing input against one VerbVariant.
// Type encodes both how complete the parse is and why it stopped.
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

    // Number of input words this parse accounted for (0 if no parameter matched)
    public int getConsumedWordCount() {
        return parameterResults.getLast().endWord() + 1;
    }

    public enum Type {
        FULL(Validity.FULL),           // all parameters matched explicitly
        FULL_DEFAULT(Validity.FULL),   // remaining parameters filled by defaults

        PARTIAL_MISSING_PARAMETERS(Validity.PARTIAL),  // more parameters required
        PARTIAL_INCOMPLETE_PARAMETER(Validity.PARTIAL), // last parameter unterminated

        NONE_TOO_MANY_WORDS(Validity.NONE), // input has extra trailing words
        NONE_NO_MATCH(Validity.NONE);       // a parameter did not match at all

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
