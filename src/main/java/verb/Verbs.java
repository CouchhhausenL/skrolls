package main.java.verb;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Verbs {
    private Verbs() {
    }

    // Keeps only fully valid results (or partially valid ones when allowed),
    // then returns those that consumed the most input words.
    public static List<VerbVariantAnalysisResult> findBestResults(List<VerbVariantAnalysisResult> results, boolean includePartial) {

        // Only elements which are fully (or partially) valid are kept
        // Only elements with the max amount of words consumed are kept
        return results
                .stream()
                .filter(result -> result.isFullyValid() || (includePartial && result.isPartiallyValid()))
                .collect(Collectors.groupingBy(VerbVariantAnalysisResult::getConsumedWordCount))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(List.of());
    }

    public static boolean hasFullyValidResult(List<VerbVariantAnalysisResult> results) {
        for (VerbVariantAnalysisResult result : results) {
            if (result.isFullyValid())
                return true;
        }
        return false;
    }
}
