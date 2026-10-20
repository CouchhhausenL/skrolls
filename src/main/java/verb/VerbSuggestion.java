package main.java.verb;

public record VerbSuggestion(
        String content,
        String completion,
        int completionOffset,
        boolean isDefault
) {
    public VerbSuggestion(String content, String input, boolean isDefault) {
        this(
                content,
                content.substring(input.length()),
                input.length(),
                isDefault
        );
    }
}
