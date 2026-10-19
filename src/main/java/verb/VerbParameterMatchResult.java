package main.java.verb;

public enum VerbParameterMatchResult {
    MATCH_AND_PARTIAL,
    MATCH,
    PARTIAL,
    NO_MATCH;

    public boolean isSomeMatch() {
        return this == MATCH || this == MATCH_AND_PARTIAL;
    }
}
