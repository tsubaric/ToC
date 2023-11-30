import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PDAGraphVizPrinter {

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
                            // Switched inputSymbol and toState places in the label
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

    public static void main(String[] args) {
        // Example usage
        Set<String> states = Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        Set<String> inputAlphabet = Set.of("'a'", "'b'", ".");
        Set<String> stackAlphabet = Set.of("!", "a", "b", "S", "A", "C");
        Map<String, Map<String, Map<String, Set<String>>>> transitions = new HashMap<>();

        // Adding transitions using the addTransition method
        PushdownAutomaton pda = new PushdownAutomaton(states, inputAlphabet, stackAlphabet, transitions,
                "0", "+!", Set.of("3"));

        pda.addTransition("0", "1", ".", Set.of("+!"));
        pda.addTransition("1", "2", ".", Set.of("+S"));
        pda.addTransition("2", "2", "'a'", Set.of("-a"));
        pda.addTransition("2", "2", "'b'", Set.of("-b"));
        pda.addTransition("2", "3", ".", Set.of("-!"));

        pda.addTransition("2", "4", ".", Set.of("-S"));
        pda.addTransition("4", "5", ".", Set.of("+A"));
        pda.addTransition("5", "2", ".", Set.of("+b"));

        pda.addTransition("2", "6", ".", Set.of("-S"));
        pda.addTransition("6", "7", ".", Set.of("+b"));
        pda.addTransition("7", "2", ".", Set.of("+A"));

        pda.addTransition("2", "8", ".", Set.of("-A"));
        pda.addTransition("8", "9", ".", Set.of("+C"));
        pda.addTransition("9", "10", ".", Set.of("+A"));
        pda.addTransition("10", "2", ".", Set.of("+C"));

        pda.addTransition("2", "11", ".", Set.of("-C"));
        pda.addTransition("11", "2", ".", Set.of("+a"));

        pda.addTransition("2", "12", ".", Set.of("-C"));
        pda.addTransition("12", "2", ".", Set.of("+b"));

        printPDAGraphViz(pda, "test.gv");
    }
}