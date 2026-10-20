package main.java.verb;

import java.util.List;

// Provides the verbs a component contributes to the input system
public interface VerbSupplier {
    List<Verb> getVerbs();
}
