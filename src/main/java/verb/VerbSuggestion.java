package main.java.verb;

// Autocomplete suggestion for the verb input widget.
// content is the full candidate; completion is the not-yet-typed suffix.
public record VerbSuggestion(
        String content,
        String completion,
        int completionOffset,
        boolean isDefault
) {
    // Derives the completion from how much of the candidate the user already typed
    public VerbSuggestion(String content, String input, boolean isDefault) {
        this(
                content,
                content.substring(input.length()),
                input.length(),
                isDefault
        );
    }
}
