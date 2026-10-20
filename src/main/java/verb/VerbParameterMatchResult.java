package main.java.verb;

// How well a parameter value matched the raw input.
// MATCH_AND_PARTIAL means it fully matches but is also a prefix of a longer
// valid value, so parsing must try both interpretations.
public enum VerbParameterMatchResult {
    MATCH_AND_PARTIAL,
    MATCH,
    PARTIAL,
    NO_MATCH;

    public boolean isSomeMatch() {
        return this == MATCH || this == MATCH_AND_PARTIAL;
    }
}
