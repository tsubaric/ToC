* Authors: Tommy Subaric & Alec Knobloch

* Tooling:
- The implementation is in Java. No additional libraries need to be installed as it uses standard Java libraries.

* Sources: Class Notes In-class examples from October 12th and 17th, and stackOverflow

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
-------------------------------------------
* CFGtoCNF class:

1. User Input and CFG Representation:
- The program takes the number of lines as input, where each line represents a production rule in the CFG.
- The CFG rules are stored in a map named mapVariableProduction, where each variable maps to a list of its productions.

2. CFG to CNF Conversion:
- The convertCFGtoCNF method is called to start the conversion process.
- The CFG is transformed to CNF through several steps:
    + Removal of epsilon productions (removeEpselon method).
    + Removal of single-variable productions (eliminateSingleVariable method).
    + Ensuring each production has only two variables or one terminal (onlyTwoTerminalandOneVariable method).
    + Handling three-terminal productions (eliminateThreeTerminal method).
    + Removing duplicate key-value pairs (removeDuplicateKeyValue method).
-------------------------------------------

Status:
CFG to PDA:
This does not work 100% I have gotten stuck and frustrated trying to debug. My current results for the Palindrome example is -->

0 -> 1 [label = ". , +!"];
0 -> 2 [label = ". , +a"]; <-- x
1 -> 2 [label = ". , +S"];
2 -> 2 [label = "a , -a"];
2 -> 2 [label = "b , -b"];
2 -> 2 [label = ". , -b"]; <-- x
2 -> 2 [label = ". , -S"];
2 -> 3 [label = ". , -!"];
4 -> 5 [label = ". , +a"];
5 -> 6 [label = ". , +S"];
6 -> 2 [label = ". , +a"];
8 -> 9 [label = ". , +b"];
9 -> 10 [label = ". , +S"];
10 -> 2 [label = ". , +b"];

-- Missing 2 -> 4 [label = ". , -S"];
-- Missing 2 -> 8 [label = ". , -S"];

- You can see that for some reason it also wants to add an "a" from state 1 to 2.
- Also you can see that It is not getting the -S when leaving state 2. Also it seems to be off by 1 because it is skipping 4. 

CFG to CNF:
This also does not work 100 % it does not completely break everything up it leaves in like a CYK form. 

For example the input: 
T->T*A|A
A->(T)|x

In my code has the final form of: 

T -> [TG, (H, x]
A -> [(H, x]
G -> [*A]
H -> [T)]

We would have liked it to return like...

B -> *
C -> (
D -> )
E -> x
T -> x
A -> x
T -> T Z
Z -> B A
T -> C Y
Y -> T D
A -> C Y
