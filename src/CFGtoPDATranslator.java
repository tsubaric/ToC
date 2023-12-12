import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CFGtoPDATranslator {

    // Declare nonTerminal and productionIndex as class variables
    private static String nonTerminal;
    private static int productionIndex;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the number of lines in the CFG
        System.out.print("Enter the number of lines in the CFG: ");
        int numLines = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Example usage:
        Set<String> nonTerminals = new HashSet<>();
        Set<String> terminals = new HashSet<>();
        Map<String, Set<String>> productions = new HashMap<>();

        // Prompt the user to enter the CFG productions
        for (int i = 0; i < numLines; i++) {
            System.out.print("Enter production " + (i + 1) + ": ");
            String production = scanner.nextLine();
            String[] parts = production.split("->");

            String nonTerminal = parts[0].trim();
            nonTerminals.add(nonTerminal);

            if (parts.length > 1) {
                String productionBody = parts[1].trim();
                productions.put(nonTerminal, new HashSet<>(Arrays.asList(productionBody.split("\\|"))));
                terminals.addAll(Arrays.asList(productionBody.split("\\s")));
            } else {
                productions.put(nonTerminal, new HashSet<>(Collections.singletonList("")));
            }
        }

        System.out.print("Enter the start symbol: ");
        String startSymbol = scanner.nextLine();

        ContextFreeGrammar cfg = new ContextFreeGrammar(nonTerminals, terminals, productions, startSymbol);

        PushdownAutomaton pda = translateCFGtoPDA(cfg);
        printPDAGraphViz(pda, "test_1.gv");

        scanner.close();
    }

    private static void addPDATransition(Map<String, Map<String, Map<String, Set<String>>>> transitions,
                                          String fromState, String toState, String inputSymbol, String stackSymbol) {
        transitions.putIfAbsent(fromState, new HashMap<>());
        transitions.get(fromState).putIfAbsent(toState, new HashMap<>());
        transitions.get(fromState).get(toState).putIfAbsent(inputSymbol, new HashSet<>());
        transitions.get(fromState).get(toState).get(inputSymbol).add(stackSymbol);
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

                // Skip null or transitions where toState is a letter
                if (fromState == null || fromState.matches("[a-zA-Z]")) {
                    continue;
                }

                for (Map.Entry<String, Map<String, Set<String>>> inputTransition : entry.getValue().entrySet()) {
                    String toState = inputTransition.getKey();

                    // Skip transitions where toState is a letter
                    if (toState.matches("[a-zA-Z]")) {
                        continue;
                    }

                    for (Map.Entry<String, Set<String>> toTransition : inputTransition.getValue().entrySet()) {
                        String inputSymbol = toTransition.getKey();

                        for (String stackSymbol : toTransition.getValue()) {
                            // Determine whether to add "+" or "-" based on the fromState
                            String stackOperation = (fromState.equals("2")) ? "-" : "+";

                            // Switched inputSymbol and toState places in the label
                            writer.println(fromState + " -> " + toState + " [label = \"" + inputSymbol + " , " + stackOperation + stackSymbol + "\"];");
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
    
        Set<String> pdaStates = new HashSet<>();
        Set<String> pdaInputAlphabet = new HashSet<>(cfg.getTerminals());
        Set<String> pdaStackAlphabet = new HashSet<>();
        Map<String, Map<String, Map<String, Set<String>>>> pdaTransitions = new HashMap<>();
        String pdaStartState = "0";
        String pdaStartStackSymbol = "!";
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
    
                // Add transitions for the production
                addProductionTransitions(pdaTransitions, state, production, nonTerminal);
            }
        }
    
        // Set the start state and stack symbol
        pdaStates.add(pdaStartState);
        pdaStackAlphabet.add(pdaStartStackSymbol);
    
        // Transition from start state to a new state for popping the initial stack symbol
        addPDATransition(pdaTransitions, "0", "1", ".", pdaStartStackSymbol);
        addPDATransition(pdaTransitions, "1", "2", ".", "S");
        addPDATransition(pdaTransitions, "2", "3", ".", pdaStartStackSymbol);
    
        // Set the accepting states
        pdaAcceptingStates.add("3");
    
        // Create the Pushdown Automaton
        return new PushdownAutomaton(pdaStates, pdaInputAlphabet, pdaStackAlphabet,
                pdaTransitions, pdaStartState, pdaStartStackSymbol, pdaAcceptingStates);
    }

    // Add transitions to the PDA for each production
    private static void addProductionTransitions(Map<String, Map<String, Map<String, Set<String>>>> transitions,
                                                String state, String production, String stackSymbol) {
        String[] symbols = production.split(" ");

        // Loop through the symbols in the production
        for (int i = 0; i < symbols.length; i++) {
            String symbol = symbols[i];
            String nextState;

            if (i == symbols.length - 1) {
                nextState = "" + productionIndex++;
                nextState = (stackSymbol.equals("2")) ? "2" : nonTerminal;
            } else {
                nextState = "" + productionIndex++;

                // Add transition to read the terminal from the input
                addPDATransition(transitions, state, nextState, ".", symbol);
            }

            // Add transition to pop the stack symbol
            addPDATransition(transitions, nextState, state, ".", stackSymbol);
        }
    }
}