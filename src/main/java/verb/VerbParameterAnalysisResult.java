package main.java.verb;

// Result of matching one parameter against a slice of the input.
// startWord/endWord delimit the inclusive word range that was consumed.
public record VerbParameterAnalysisResult(
        VerbParameter<?> parameter,
        Validity validity,
        String input,
        int startWord,
        int endWord
) {

    public boolean isFullyValid() {
        return validity == Validity.FULL;
    }

    public boolean isPartiallyValid() {
        return validity == Validity.PARTIAL;
    }

    public Object getValue() {
        return parameter.getValue(input);
    }

    public enum Validity {
        FULL,
        PARTIAL
    }
}
