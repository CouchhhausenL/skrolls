package main.java.verb;

import java.util.*;

// Resolves parsed parameters to typed values for the execution callback.
// Values from a variant that only exists in some variants are rejected on
// access, because they may be absent for other variants at runtime.
public class VerbExecutionContext {
    private final Map<String, Object> nameParameterValueMap = new HashMap<>();
    private final List<String> nonGlobalNames = new ArrayList<>();

    public VerbExecutionContext(Verb verb, VerbVariantAnalysisResult result) {
        if (result == null) return;

        // Seed the map with every default value of the matched variant
        for (VerbParameter<?> parameter : result.verbVariant().getParameters()) {
            if (parameter.hasDefaultValue() && parameter.hasName())
                nameParameterValueMap.put(parameter.getName(), parameter.getDefaultValue());
        }

        // Overwrite defaults with the values the user actually typed
        for (VerbParameterAnalysisResult parameterResult : result.parameterResults()) {
            if (!parameterResult.parameter().hasName()) continue;
            nameParameterValueMap.put(parameterResult.parameter().getName(), parameterResult.getValue());
        }

        // Mark names that are not shared by all variants; reading those is unsafe
        for (String name : nameParameterValueMap.keySet()) {
            if (!verb.allVariantsHaveParameterName(name))
                nonGlobalNames.add(name);
        }
    }

    private Object get(String name) {
        Objects.requireNonNull(name, "name must not be null");

        if (nonGlobalNames.contains(name))
            throw new IllegalArgumentException("Using a name which is not existent in all variants is potentially dangerous");
        if (!nameParameterValueMap.containsKey(name))
            throw new IllegalArgumentException("Value map does not contain name");

        return nameParameterValueMap.get(name);
    }

    public String getString(String name) {
        if (get(name) instanceof String string)
            return string;

        throw new IllegalArgumentException("Called getString for a non-string parameter");
    }

    public Integer getInteger(String name) {
        if (get(name) instanceof Integer integer)
            return integer;

        throw new IllegalArgumentException("Called getInteger for a non-integer parameter");
    }
}
