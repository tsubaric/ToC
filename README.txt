* Authors: Tommy Subaric & Alec Knobloch

* Tooling:
- The implementation is in Java. No additional libraries need to be installed as it uses standard Java libraries.

* Sources:

* Required functions:
-------------------------------------------
* ContextFreeGrammar class:
    - Represents a context-free grammar.
    - Contains non-terminals, terminals, productions, and start symbol.
    - Provides getters and setters for its components.
-------------------------------------------
* PushdownAutomaton class:
    - Represents a pushdown automaton.
    - Contains states, input alphabet, stack alphabet, transitions, start state, start stack symbol, and accepting states.
    - Provides getters and setters for its components.
    - Provides a method to add transitions.
-------------------------------------------
* CFGtoPDATranslator class:
1. main method in CFGtoPDATranslator:
    - Reads input from the user to create a Context-Free Grammar (CFG).
    - Translates the CFG to a Pushdown Automaton (PDA).
    - Prints the PDA in Graphviz format to a file via "output.gv".

2. addPDATransition method in CFGtoPDATranslator:
    - Adds transitions to the PDA.

3. printPDAGraphViz method in CFGtoPDATranslator:
    - Prints the PDA transitions in Graphviz format to a file.

4. translateCFGtoPDA method in CFGtoPDATranslator:
    - Translates a given CFG to a PDA.

5. addProductionTransitions method in CFGtoPDATranslator:
    - Adds transitions based on the CFG productions to the PDA.
-------------------------------------------
* CFGtoCNF class:
- ....
-------------------------------------------

Status: