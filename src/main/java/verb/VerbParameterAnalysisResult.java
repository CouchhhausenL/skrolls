package main.java.verb;

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
