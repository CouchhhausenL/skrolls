package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.DrawContextView;
import main.java.util.Color;
import main.java.verb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// Command input with live autocompletion for game verbs.
// suggestions is the list shown above the input; executionConsumer is set when
// the current text unambiguously resolves to a runnable command.
public class VerbInputWidget extends AbstractTextFieldWidget {
    private final GameContext context;
    private final List<VerbSuggestion> suggestions = new ArrayList<>();
    private final Colors colors;
    private Consumer<GameContext> executionConsumer = null;

    public static final Colors DEFAULT_COLORS = new Colors(
            0x101010,
            0x202020,
            0x404040,
            Color.WHITE,
            0xA0FFA0,
            0xD0D0D0,
            0x909090,
            0x505050,
            0x303030,
            0xA0A0FF
    );

    public VerbInputWidget(GameContext context, int x, int y, int width, int height, String content, Colors colors) {
        super(x, y, width, height, content);
        this.context = context;
        this.colors = colors;
        update();
    }

    public VerbInputWidget(GameContext context, int x, int y, int width, int height, Colors colors) {
        this(context, x, y, width, height, "", colors);
    }

    public VerbInputWidget(GameContext context, int x, int y, int width, int height) {
        this(context, x, y, width, height, DEFAULT_COLORS);
    }

    @Override
    protected void onDraw(DrawContextView context) {
        context.fill(0, 0, getWidth(), getHeight(), colors.background());
        // Last row is the input field; rows above show suggestions
        int yOffset = getHeight() - 1;
        for (int y = 0; y < yOffset; y++) {
            if (y > suggestions.size() - 1) continue;
            VerbSuggestion suggestion = suggestions.get(y);

            // Chevron color signals the state: default value, single completion
            // candidate (tab hint) or already fully typed
            int chevronColor = colors.chevron();
            if (suggestions.size() == 1)
                chevronColor = colors.chevronAutocompletionHint();
            if (suggestion.completion().isEmpty())
                chevronColor = colors.chevronTyped();

            context.drawString(0, y, suggestion.isDefault() ? "»" : "›", chevronColor);
            context.drawString(2, y, suggestion.content(), colors.typedSuggestion());
            context.drawString(2 + suggestion.completionOffset(), y, suggestion.completion(), colors.suggestion());
        }
        context.fill(0, yOffset, getWidth(), getHeight(), colors.inputBackground());
        drawCursor(context, 0, yOffset, colors.cursor());
        // Text turns green when the command is executable
        drawVisibleText(context, 0, yOffset, executionConsumer != null ? colors.correctContent() : colors.content());
    }

    // Leading spaces are ignored; recompute suggestions on every change
    @Override
    public void setContent(String content) {
        super.setContent(content.stripLeading());
        update();
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        super.onKeyInput(event);
        // TAB completes only when there is a single unambiguous candidate
        if (event.isTab()) {
            for (VerbSuggestion suggestion : suggestions) {
                if (suggestions.size() != 1) continue;
                setContent(getContent() + suggestion.completion());
                cursor = getContent().length();
                break;
            }
        }
        // ENTER runs the command and clears the field on success
        if (event.isEnter()) {
            if (execute()) {
                cursor = 0;
                setContent("");
            }
        }
    }

    // Runs the resolved command; false when the text is not executable
    private boolean execute() {
        if (executionConsumer == null) return false;
        executionConsumer.accept(context);
        return true;
    }

    // Recomputes suggestions and executability from the current text.
    // Before the first space only verb names are completed; afterwards the
    // named verb's parameters are analyzed and completed.
    private void update() {
        suggestions.clear();
        executionConsumer = null;

        List<Verb> verbs = context.getVerbs();

        String content = getContent();
        // Still typing the command name itself
        if (!content.contains(" ")) {
            for (Verb verb : verbs) {
                // A verb without variants executes as soon as its name matches
                if (verb.matchesNameOrAlias(content) && verb.hasNoVariants())
                    executionConsumer = (c) -> verb.execute(null, c);

                for (String suggestionString : verb.suggest(content)) {
                    VerbSuggestion suggestion = new VerbSuggestion(
                            suggestionString,
                            content,
                            false
                    );

                    if (!suggestions.contains(suggestion))
                        suggestions.add(suggestion);
                }
            }
            return;
        }

        // Split into command name and the remaining arguments
        String[] split = content.split("\\s+", 2);
        String name = split[0];
        String rest = split[1];

        for (Verb verb : verbs) {
            if (!verb.matchesNameOrAlias(name)) continue;

            // Parse the arguments against all variants of this verb
            List<VerbVariantAnalysisResult> analysisResults = verb.analyze(rest);

            // Exactly one fully valid parse means the command can be executed
            List<VerbVariantAnalysisResult> bestResults = Verbs.findBestResults(analysisResults, false);
            if (bestResults.size() == 1)
                executionConsumer = (c) -> verb.execute(bestResults.getFirst(), c);

            // Suggest continuations from the last parameter of the best
            // partial parses as well
            for (VerbVariantAnalysisResult result : Verbs.findBestResults(analysisResults, true)) {
                VerbParameterAnalysisResult parameter = result.parameterResults().getLast();
                for (String stringSuggestion : parameter.parameter().suggest(parameter.input())) {
                    VerbSuggestion suggestion = new VerbSuggestion(
                            stringSuggestion,
                            parameter.input(),
                            parameter.parameter().hasDefaultValue() &&
                                    String.valueOf(parameter.parameter().getDefaultValue()).equals(stringSuggestion)
                    );

                    // Default values are promoted to the top of the list
                    if (!suggestions.contains(suggestion)) {
                        if (suggestion.isDefault()) {
                            suggestions.addFirst(suggestion);
                        } else {
                            suggestions.add(suggestion);
                        }
                    }
                }
            }
        }
    }

    public record Colors(
            int background,
            int inputBackground,
            int cursor,
            int content,
            int correctContent,
            int suggestion,
            int typedSuggestion,
            int chevron,
            int chevronTyped,
            int chevronAutocompletionHint
    ) {
    }
}
