import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CFGtoPDATranslator {

    public static void main(String[] args) {
        // Example usage:
        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "A", "C"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b"));
        Map<String, Set<String>> productions = new HashMap<>();
        productions.put("S", new HashSet<>(Arrays.asList("Ab", "bA")));
        productions.put("A", new HashSet<>(Arrays.asList("a", "CAC")));
        productions.put("C", new HashSet<>(Arrays.asList("a", "b")));

        String startSymbol = "S";

        ContextFreeGrammar cfg = new ContextFreeGrammar(nonTerminals, terminals, productions, startSymbol);

        PushdownAutomaton pda = translateCFGtoPDA(cfg);
        printPDAGraphViz(pda, "test_1.gv");
    }

    public static void printPDAGraphViz(PushdownAutomaton pda, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("digraph pda {");
            writer.println("rankdir = LR;");
            writer.println("hidden [shape = plaintext, label = \"\"];");
            writer.println("node [shape = doublecircle];");

            // Print accepting states
            for (String state : pda.getAcceptingStates()) {
                writer.println(state + ";");
            }

            writer.println("node [shape = circle];");
            writer.println("hidden -> " + pda.getStartState() + ";");

            // Print transitions
            for (Map.Entry<String, Map<String, Map<String, Set<String>>>> entry : pda.getTransitions().entrySet()) {
                String fromState = entry.getKey();

                for (Map.Entry<String, Map<String, Set<String>>> inputTransition : entry.getValue().entrySet()) {
                    String toState = inputTransition.getKey();

                    for (Map.Entry<String, Set<String>> toTransition : inputTransition.getValue().entrySet()) {
                        String inputSymbol = toTransition.getKey();

                        for (String stackSymbol : toTransition.getValue()) {
                            writer.println(fromState + " -> " + toState + " [label = \"" + inputSymbol + " , " + stackSymbol + "\"];");
                        }
                    }
                }
            }
            writer.println("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PushdownAutomaton translateCFGtoPDA(ContextFreeGrammar cfg) {
        System.out.println("Translating CFG to PDA...");
    
        // Initialize PDA components
        Set<String> pdaStates = new HashSet<>();
        Set<String> pdaInputAlphabet = new HashSet<>(cfg.getTerminals());
        Set<String> pdaStackAlphabet = new HashSet<>(cfg.getTerminals());
        pdaStackAlphabet.add("");  // Include the empty stack symbol
        Map<String, Map<String, Map<String, Set<String>>>> pdaTransitions = new HashMap<>();
        String pdaStartState = "0";
        String pdaStartStackSymbol = "+!";
        Set<String> pdaAcceptingStates = new HashSet<>();
    
        // Create a new PDA state for each CFG non-terminal
        for (String nonTerminal : cfg.getNonTerminals()) {
            pdaStates.add(nonTerminal);
        }
    
        // Create a new PDA state for each CFG production
        int productionIndex = 1;
        for (Map.Entry<String, Set<String>> entry : cfg.getProductions().entrySet()) {
            String nonTerminal = entry.getKey();
            Set<String> productionSet = entry.getValue();
    
            for (String production : productionSet) {
                String state = "" + productionIndex++;
                pdaStates.add(state);
    
                // Add a transition for each symbol in the production
                String[] symbols = production.split(" ");
                for (int i = 0; i < symbols.length; i++) {
                    String symbol = symbols[i];
                    String nextState = (i == symbols.length - 1) ? nonTerminal : "" + productionIndex++;
                    addPDATransition(pdaTransitions, state, nextState, ".", "+" + symbol);
                }
            }
        }
    
        // Set the start state and stack symbol
        pdaStates.add(pdaStartState);
        pdaStackAlphabet.add(pdaStartStackSymbol);
        addPDATransition(pdaTransitions, pdaStartState, cfg.getStartSymbol(), ".", pdaStartStackSymbol);
    
        // Set the accepting states
        pdaAcceptingStates.add(pdaStartState);
    
        // Create the Pushdown Automaton
        return new PushdownAutomaton(pdaStates, pdaInputAlphabet, pdaStackAlphabet,
                pdaTransitions, pdaStartState, pdaStartStackSymbol, pdaAcceptingStates);
    }

    private static void addPDATransition(Map<String, Map<String, Map<String, Set<String>>>> transitions,
                                          String fromState, String toState, String inputSymbol, String stackSymbol) {
        transitions.putIfAbsent(fromState, new HashMap<>());
        transitions.get(fromState).putIfAbsent(toState, new HashMap<>());
        transitions.get(fromState).get(toState).putIfAbsent(inputSymbol, new HashSet<>());
        transitions.get(fromState).get(toState).get(inputSymbol).add(stackSymbol);
    }
}
