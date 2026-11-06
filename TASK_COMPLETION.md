## Task Completion

- User input has a direct effect on the game
- The program has no known errors, except those listed in `Shortcomings`
- The program is well commented, AI was used, check `Aids / Tools`
- The program uses many different classes
- The program respects OOP principles by using getters/setters and limited view-like contexts
- The program uses OOP concepts such as:
    - Inheritance
    - Encapsulation
    - Polymorphism
    - Abstraction (abstract classes and interfaces)
- The program uses Java language features complementing OOP such as:
    - Generics
    - Records
    - Enums
    - Exceptions
- A class diagram exists, see `Class Diagram` and `Aids / Tools`

## Class Diagram

Complete:
![Complete class diagram](./docs/uml-class-diagram_complete.svg)

Minified: (Scene and Spell boilerplate removed)
![Minified class diagram](./docs/uml-class-diagram_minified.svg)

## Aids / Tools

See [AI_DISCLOSURE.md](./AI_DISCLOSURE.md)

- What tools were used for:
    - Generating UML class diagrams
    - Editing .ska animations
    - Version control

## Shortcomings

- The project has grown far beyond what the task was meant to encompass and therefore:
    - Has 6868+ lines of code (13000+ when counting boilerplate definitions)
    - Has 144+ source files (265+ when counting boilerplate scene and spell definitions)
    - Relies on many critical systems (screen, widget, scene) which are not strictly necessary for a 1-week project
- Many planned features were never fully implemented in-game:
    - Storyline
    - Crystallized mana currency system
    - Interactables in scenes
    - Warping system
    - Save-state system
    - ...
- Many classes/features are rushed and therefore:
    - Not completely integrated
    - Not written to the highest of OOP standards
    - Missing (non-necessary) @NotNull and @Nullable
    - Missing concrete null checks in many places
    - Missing setter checks in some places