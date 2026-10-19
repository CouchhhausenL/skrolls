package main.java.verb;

import java.util.*;

public class VerbExecutionContext {
    private final Map<String, Object> nameParameterValueMap = new HashMap<>();
    private final List<String> nonGlobalNames = new ArrayList<>();

    public VerbExecutionContext(Verb verb, VerbVariantAnalysisResult result) {
        // Add all defaults to the map
        for (VerbParameter<?> parameter : result.verbVariant().getParameters()) {
            if (parameter.hasDefaultValue() && parameter.hasName())
                nameParameterValueMap.put(parameter.getName(), parameter.getDefaultValue());
        }

        // Add all specified parameter values to the map
        // This may overwrite defaults, as expected
        for (VerbParameterAnalysisResult parameterResult : result.parameterResults()) {
            if (!parameterResult.parameter().hasName()) continue;
            nameParameterValueMap.put(parameterResult.parameter().getName(), parameterResult.getValue());
        }

        // Mark all names which do not exist in all variants
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
