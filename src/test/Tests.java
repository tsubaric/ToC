package test;

public class Tests {

     // Example Test 1
        // Set<String> states = Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        // Set<String> inputAlphabet = Set.of("'a'", "'b'", ".");
        // Set<String> stackAlphabet = Set.of("!", "a", "b", "S");
        // Map<String, Map<String, Map<String, Set<String>>>> transitions = new HashMap<>();

        // // Adding transitions using the addTransition method
        // PushdownAutomaton pda = new PushdownAutomaton(states, inputAlphabet, stackAlphabet, transitions,
        //         "0", "+!", Set.of("3"));

        // pda.addTransition("2", "2", "'a'", Set.of("-a"));
        // pda.addTransition("2", "2", "'b'", Set.of("-b"));
        // pda.addTransition("0", "1", ".", Set.of("+!"));
        // pda.addTransition("1", "2", ".", Set.of("+S"));
        // pda.addTransition("2", "3", ".", Set.of("-!"));
        // pda.addTransition("2", "2", ".", Set.of("-S"));
        // pda.addTransition("2", "4", ".", Set.of("-S"));
        // pda.addTransition("4", "5", ".", Set.of("+a"));
        // pda.addTransition("5", "6", ".", Set.of("+S"));
        // pda.addTransition("6", "2", ".", Set.of("+a"));
        // pda.addTransition("2", "7", ".", Set.of("-S"));
        // pda.addTransition("7", "8", ".", Set.of("+b"));
        // pda.addTransition("8", "9", ".", Set.of("+S"));
        // pda.addTransition("9", "2", ".", Set.of("+b"));

        // printPDAGraphViz(pda, "test.gv");

        //////////////////////
        //////////////////////
        
        // Example Test 2
        // Set<String> states = Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        // Set<String> inputAlphabet = Set.of("'a'", "'b'", ".");
        // Set<String> stackAlphabet = Set.of("!", "a", "b", "S", "A", "C");
        // Map<String, Map<String, Map<String, Set<String>>>> transitions = new HashMap<>();

        // // Adding transitions using the addTransition method
        // PushdownAutomaton pda = new PushdownAutomaton(states, inputAlphabet, stackAlphabet, transitions,
        //         "0", "+!", Set.of("3"));

        // pda.addTransition("0", "1", ".", Set.of("+!"));
        // pda.addTransition("1", "2", ".", Set.of("+S"));
        // pda.addTransition("2", "2", "'a'", Set.of("-a"));
        // pda.addTransition("2", "2", "'b'", Set.of("-b"));
        // pda.addTransition("2", "3", ".", Set.of("-!"));

        // pda.addTransition("2", "4", ".", Set.of("-S"));
        // pda.addTransition("4", "5", ".", Set.of("+A"));
        // pda.addTransition("5", "2", ".", Set.of("+b"));

        // pda.addTransition("2", "6", ".", Set.of("-S"));
        // pda.addTransition("6", "7", ".", Set.of("+b"));
        // pda.addTransition("7", "2", ".", Set.of("+A"));

        // pda.addTransition("2", "8", ".", Set.of("-A"));
        // pda.addTransition("8", "9", ".", Set.of("+C"));
        // pda.addTransition("9", "10", ".", Set.of("+A"));
        // pda.addTransition("10", "2", ".", Set.of("+C"));

        // pda.addTransition("2", "11", ".", Set.of("-C"));
        // pda.addTransition("11", "2", ".", Set.of("+a"));

        // pda.addTransition("2", "12", ".", Set.of("-C"));
        // pda.addTransition("12", "2", ".", Set.of("+b"));

        // printPDAGraphViz(pda, "test.gv");
}
