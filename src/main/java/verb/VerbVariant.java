package main.java.verb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// One parameter layout of a Verb. analyze() tries every way of splitting the
// input across the parameters and yields all plausible parses (backtracking).
public class VerbVariant {
    private final List<VerbParameter<?>> parameters;

    private VerbVariant(List<VerbParameter<?>> parameters) {
        this.parameters = parameters;
    }

    public List<VerbVariantAnalysisResult> analyze(String input) {
        String[] words = input.stripTrailing().split("\\s+");
        // A trailing space means the user started a new (empty) word
        if (input.endsWith(" ")) {
            words = Arrays.copyOf(words, words.length + 1);
            words[words.length - 1] = "";
        }
        return analyze(words, parameters, new ArrayList<>());
    }

    private List<VerbVariantAnalysisResult> analyze(String[] words, List<VerbParameter<?>> parameters, List<VerbParameterAnalysisResult> results) {
        // The word where parsing begins, by default the first one
        int startWord = 0;
        if (!results.isEmpty()) {
            // If this is not the first parameter, start one word after where the last one ended
            startWord = results.getLast().endWord() + 1;
        }

        // Tried starting a new parameter, but ran out of words beforehand
        if (startWord > words.length - 1) {

            // The last parameter was partially valid but ran out of words
            if (!results.isEmpty() && results.getLast().isPartiallyValid())
                return List.of(new VerbVariantAnalysisResult(
                        VerbVariantAnalysisResult.Type.PARTIAL_INCOMPLETE_PARAMETER,
                        this,
                        results
                ));


            // No more parameters left and no more words left (best case scenario)
            if (parameters.isEmpty())
                return List.of(new VerbVariantAnalysisResult(
                        VerbVariantAnalysisResult.Type.FULL,
                        this,
                        results
                ));

            // Iterate remaining parameters to check if all have defaults
            for (VerbParameter<?> parameter : parameters) {
                // Not all have defaults, as the current one does not
                if (!parameter.hasDefaultValue()) {
                    // Enough words for some parameters to get finished, but not all
                    return List.of(new VerbVariantAnalysisResult(
                            VerbVariantAnalysisResult.Type.PARTIAL_MISSING_PARAMETERS,
                            this,
                            results
                    ));
                }
            }

            // All remaining parameters have defaults (second-best case scenario)
            return List.of(new VerbVariantAnalysisResult(
                    VerbVariantAnalysisResult.Type.FULL_DEFAULT,
                    this,
                    results
            ));
        }

        // No more parameters to parse, but still more unparsed words
        if (parameters.isEmpty()) {
            return List.of(new VerbVariantAnalysisResult(
                    VerbVariantAnalysisResult.Type.NONE_TOO_MANY_WORDS,
                    this,
                    results
            ));
        }

        int wordCount = 1;
        // Current parameter to parse, always the first one as the already parsed one gets removed
        VerbParameter<?> parameter = parameters.getFirst();
        List<VerbParameterAnalysisResult> possibilities = new ArrayList<>();

        // Greedily grow the slice covering the current parameter.
        // PARTIAL / MATCH_AND_PARTIAL results mean a longer slice could
        // still match, so keep extending word by word.
        while (true) {
            // The current slice of the input
            String currentSlice = getWordsSlice(words, startWord, wordCount);

            VerbParameterMatchResult result = parameter.validate(currentSlice);

            // Give up if it did not match at all
            if (result == VerbParameterMatchResult.NO_MATCH) break;

            // Add a match to the results if it matched in some way
            if (result.isSomeMatch())
                possibilities.add(new VerbParameterAnalysisResult(
                        parameter,
                        VerbParameterAnalysisResult.Validity.FULL,
                        currentSlice,
                        startWord,
                        startWord + wordCount - 1
                ));

            // If it is a direct match, there is no hope for another one
            if (result == VerbParameterMatchResult.MATCH) break;

            // If it was PARTIAL or MATCH_AND_PARTIAL, continue to potentially discover more
            // Extend the slice; if it would run past the end, record a PARTIAL
            // parse and stop
            wordCount++;
            if (startWord + wordCount - 1 > words.length - 1) { // Double - 1 is easier to understand
                possibilities.add(new VerbParameterAnalysisResult(
                        parameter,
                        VerbParameterAnalysisResult.Validity.PARTIAL,
                        currentSlice,
                        startWord,
                        startWord + wordCount - 1
                ));
                break;
            }
        }

        List<VerbVariantAnalysisResult> analysisResults = new ArrayList<>();

        // The loop found nothing (broke before matching), so this branch fails
        if (possibilities.isEmpty()) analysisResults.add(new VerbVariantAnalysisResult(
                VerbVariantAnalysisResult.Type.NONE_NO_MATCH,
                this,
                results
        ));

        for (VerbParameterAnalysisResult possibility : possibilities) {
            List<VerbParameterAnalysisResult> newResults = new ArrayList<>(results);
            newResults.add(possibility);

            // Recursive call
            // First parameter removed, so the next iteration can call .getFirst()
            analysisResults.addAll(analyze(words, parameters.subList(1, parameters.size()), newResults));
        }

        // Parameter has a default
        if (parameter.hasDefaultValue()) {
            // Recursive call to another version where the default parameter is missing as it is optional
            // All results of that tree are appended as well
            analysisResults.addAll(analyze(words, parameters.subList(1, parameters.size()), results));
        }

        return analysisResults;
    }

    // Get a slice of a String[] starting at start, with the width of count
    // Index of the first word is 0
    private String getWordsSlice(String[] words, int start, int count) {

        if (start < 0)
            throw new IllegalArgumentException("start must not be negative");

        if (count <= 0)
            throw new IllegalArgumentException("count must be greater than zero");

        StringBuilder result = new StringBuilder();

        for (int i = start; i < start + count; i++) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(words[i]);
        }

        return result.toString();
    }

    public boolean hasParameterWithName(String name) {
        for (VerbParameter<?> parameter : parameters) {
            if (parameter.hasName() && parameter.getName().equals(name))
                return true;
        }
        return false;
    }

    public List<VerbParameter<?>> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public static class Builder {
        private final Verb.Builder parent;
        private final List<VerbParameter<?>> parameters = new ArrayList<>();

        public Builder(Verb.Builder parent) {
            this.parent = parent;
        }

        public Builder parameter(VerbParameter<?> parameter) {
            parameters.add(parameter);
            return this;
        }

        public ChoiceVerbParameter.Builder choice(String name, String... options) {
            return new ChoiceVerbParameter.Builder(this, name, options);
        }

        public IntegerVerbParameter.Builder integer(String name, int min, int max) {
            return new IntegerVerbParameter.Builder(this, name, min, max);
        }

        public Verb.Builder build() {
            return parent.variant(new VerbVariant(parameters));
        }
    }
}
