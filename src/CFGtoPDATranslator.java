import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CFGtoPDATranslator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of lines in the CFG: ");
        int numLines = scanner.nextInt();
        scanner.nextLine(); 

        Set<String> nonTerminals = new HashSet<>();
        Set<String> terminals = new HashSet<>();
        Map<String, Set<String>> productions = new HashMap<>();

        for (int i = 0; i < numLines; i++) {
            System.out.print("Enter production " + (i + 1) + ": ");
            String production = scanner.nextLine();
            String[] parts = production.split("->");

            String nonTerminal = parts[0].trim();
            nonTerminals.add(nonTerminal);

            if (parts.length > 1) {
                String productionBody = parts[1].trim();
                productions.put(nonTerminal, productions.getOrDefault(nonTerminal, new HashSet<>()));
                productions.put(nonTerminal, new HashSet<>(Arrays.asList(productionBody.split("\\|"))));
                //terminals.addAll(Arrays.asList(productionBody.split("\\s")));
            } else {
                productions.put(nonTerminal, productions.getOrDefault(nonTerminal, new HashSet<>()));
                productions.get(nonTerminal).add(""); 
            }
        }

        System.out.print("Enter the start symbol: ");
        String startSymbol = scanner.nextLine();

        ContextFreeGrammar cfg = new ContextFreeGrammar(nonTerminals, terminals, productions, startSymbol);

        PushdownAutomaton pda = translateCFGtoPDA(cfg);
        printPDAGraphViz(pda, "test.gv");

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

            for (String state : pda.getAcceptingStates()) {
                writer.println(state + ";");
            }

            writer.println("node [shape = circle];");
            writer.println("hidden -> " + pda.getStartState() + ";");

            for (Map.Entry<String, Map<String, Map<String, Set<String>>>> entry : pda.getTransitions().entrySet()) {
                String fromState = entry.getKey();

                if (fromState == null || fromState.matches("[a-zA-Z]")) {
                    continue;
                }

                if (fromState.equals("3")) {
                    continue; // Skip transitions from accepting state "3"
                }

                for (Map.Entry<String, Map<String, Set<String>>> inputTransition : entry.getValue().entrySet()) {
                    String toState = inputTransition.getKey();

                    if (toState.matches("[a-zA-Z]")) {
                        continue;
                    }

                    for (Map.Entry<String, Set<String>> toTransition : inputTransition.getValue().entrySet()) {
                        String inputSymbol = toTransition.getKey();

                        for (String stackSymbol : toTransition.getValue()) {
                            String stackOperation = (fromState.equals("2")) ? "-" : "+";
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

    private static PushdownAutomaton translateCFGtoPDA(ContextFreeGrammar cfg) {
        System.out.println("Translating CFG to PDA...");
    
        Set<String> pdaStates = new HashSet<>();
        Set<String> pdaInputAlphabet = new HashSet<>(cfg.getTerminals());
        Set<String> pdaStackAlphabet = new HashSet<>();
        Map<String, Map<String, Map<String, Set<String>>>> pdaTransitions = new HashMap<>();
        String pdaStartState = "0";
        String pdaStartStackSymbol = "!";
        Set<String> pdaAcceptingStates = new HashSet<>();
    
        for (String nonTerminal : cfg.getNonTerminals()) {
            pdaStates.add(nonTerminal);
        }
    
        int stateIndex = 2; // Set stateIndex to 2 directly, without an offset
    
        for (Map.Entry<String, Set<String>> entry : cfg.getProductions().entrySet()) {
            String nonTerminal = entry.getKey();
            Set<String> productionSet = entry.getValue();
    
            for (String production : productionSet) {
                String currentState = (stateIndex) + "";
                String nextState = (stateIndex + 2) + "";
                addProductionTransitions(pdaTransitions, stateIndex, production, nonTerminal);
    
                if (stateIndex < productionSet.size()) {
                    addPDATransition(pdaTransitions, currentState, nextState, ".", nonTerminal);
                }
    
                stateIndex++;
            }
    
            if (!productionSet.isEmpty()) {
                pdaAcceptingStates.add("3"); // Set accepting state to "3"
                addPDATransition(pdaTransitions, "2", "3", ".", pdaStartStackSymbol);
            }
        }
    
        pdaStates.add(pdaStartState);
        pdaStackAlphabet.add(pdaStartStackSymbol);
    
        addPDATransition(pdaTransitions, "0", "1", ".", pdaStartStackSymbol);
        addPDATransition(pdaTransitions, "1", "2", ".", cfg.getStartSymbol());
    
        return new PushdownAutomaton(pdaStates, pdaInputAlphabet, pdaStackAlphabet,
                pdaTransitions, pdaStartState, pdaStartStackSymbol, pdaAcceptingStates);
    }
    
    private static void addProductionTransitions(Map<String, Map<String, Map<String, Set<String>>>> transitions,
                                              int stateIndex, String production, String stackSymbol) {
        String[] symbols = production.split(" ");
    
        for (int i = 0; i < symbols.length; i++) {
            String symbol = symbols[i];
            String nextState;
    
            if (i == symbols.length - 1) {
                nextState = "2";
                addPDATransition(transitions, stateIndex + "", nextState, ".", symbol);
            } else {
                nextState = (stateIndex + 1) + "";
            }
            addPDATransition(transitions, stateIndex + "", nextState, ".", symbol);
            stateIndex++;
        }
    }
}