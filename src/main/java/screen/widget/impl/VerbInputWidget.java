package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.game.GameContext;
import main.java.screen.DrawContextView;
import main.java.util.Color;
import main.java.verb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VerbInputWidget extends AbstractTextFieldWidget {
    private final GameContext context;
    private final List<VerbSuggestion> suggestions = new ArrayList<>();
    private final Colors colors;
    private Consumer<GameContext> executionConsumer = null;

    public static final Colors DEFAULT_COLORS = new Colors(
            0x101010,
            0x202020,
            0x404040,
            Color.WHITE
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
        int yOffset = getHeight() - 1;
        for (int y = 0; y < yOffset; y++) {
            if (y > suggestions.size() - 1) continue;
            VerbSuggestion suggestion = suggestions.get(y);

            int chevronColor = 0x505050;
            if (suggestions.size() == 1)
                chevronColor = 0xA0A0FF;
            if (suggestion.completion().isEmpty())
                chevronColor = 0x303030;

            context.drawString(0, y, suggestion.isDefault() ? "»" : "›", chevronColor);
            context.drawString(2, y, suggestion.content(), 0x909090);
            context.drawString(2 + suggestion.completionOffset(), y, suggestion.completion(), colors.content());
        }
        context.fill(0, yOffset, getWidth(), getHeight(), colors.inputBackground());
        drawCursor(context, 0, yOffset, colors.cursor());
        drawVisibleText(context, 0, yOffset, executionConsumer != null ? 0xA0FFA0 : colors.content());
    }

    @Override
    public void setContent(String content) {
        super.setContent(content.stripLeading());
        update();
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        super.onKeyInput(event);
        if (event.isTab()) {
            for (VerbSuggestion suggestion : suggestions) {
                if (suggestions.size() != 1) continue;
                setContent(getContent() + suggestion.completion());
                cursor = getContent().length();
                break;
            }
        }
        if (event.isEnter()) {
            if (execute()) {
                cursor = 0;
                setContent("");
            }
        }
    }

    private boolean execute() {
        if (executionConsumer == null) return false;
        executionConsumer.accept(context);
        return true;
    }

    private void update() {
        suggestions.clear();
        executionConsumer = null;

        List<Verb> verbs = context.getVerbs();

        String content = getContent();
        if (!content.contains(" ")) {
            for (Verb verb : verbs) {
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

        String[] split = content.split("\\s+", 2);
        String name = split[0];
        String rest = split[1];

        for (Verb verb : verbs) {
            if (!verb.matchesNameOrAlias(name)) continue;

            List<VerbVariantAnalysisResult> analysisResults = verb.analyze(rest);

            List<VerbVariantAnalysisResult> bestResults = Verbs.findBestResults(analysisResults, false);
            if (bestResults.size() == 1)
                executionConsumer = (c) -> verb.execute(bestResults.getFirst(), c);

            for (VerbVariantAnalysisResult result : Verbs.findBestResults(analysisResults, true)) {
                VerbParameterAnalysisResult parameter = result.parameterResults().getLast();
                for (String stringSuggestion : parameter.parameter().suggest(parameter.input())) {
                    VerbSuggestion suggestion = new VerbSuggestion(
                            stringSuggestion,
                            parameter.input(),
                            parameter.parameter().hasDefaultValue() &&
                                    String.valueOf(parameter.parameter().getDefaultValue()).equals(stringSuggestion)
                    );

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

    public record Colors(int background, int inputBackground, int cursor, int content) {
    }
}
