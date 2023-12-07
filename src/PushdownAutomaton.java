import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PushdownAutomaton {
    private Set<String> states;
    private Set<String> inputAlphabet;
    private Set<String> stackAlphabet;
    private Map<String, Map<String, Map<String, Set<String>>>> transitions;
    private String startState;
    private String startStackSymbol;
    private Set<String> acceptingStates;

    // Getter for states
    public Set<String> getStates() {
        return states;
    }

    // Setter for states
    public void setStates(Set<String> states) {
        if (states == null) {
            throw new IllegalArgumentException("States cannot be null");
        }
        this.states = states;
    }

    // Getter for inputAlphabet
    public Set<String> getInputAlphabet() {
        return inputAlphabet;
    }

    // Setter for inputAlphabet
    public void setInputAlphabet(Set<String> inputAlphabet) {
        if (inputAlphabet == null) {
            throw new IllegalArgumentException("Input Alphabet cannot be null");
        }
        this.inputAlphabet = inputAlphabet;
    }

    // Getter for stackAlphabet
    public Set<String> getStackAlphabet() {
        return stackAlphabet;
    }

    // Setter for stackAlphabet
    public void setStackAlphabet(Set<String> stackAlphabet) {
        if (stackAlphabet == null) {
            throw new IllegalArgumentException("Stack Alphabet cannot be null");
        }
        this.stackAlphabet = stackAlphabet;
    }

    // Getter for transitions
    public Map<String, Map<String, Map<String, Set<String>>>> getTransitions() {
        return transitions;
    }

    // Setter for transitions
    public void setTransitions(Map<String, Map<String, Map<String, Set<String>>>> transitions) {
        if (transitions == null) {
            throw new IllegalArgumentException("Transactions cannot be null");
        }
        this.transitions = transitions;
    }

    public void addTransition(String fromState, String toState, String inputSymbol, Set<String> stackSymbols) {
        // Check if fromState exists in transitions
        transitions.putIfAbsent(fromState, new HashMap<>());

        // Check if toState exists in transitions for fromState
        transitions.get(fromState).putIfAbsent(toState, new HashMap<>());

        // Add the transition
        transitions.get(fromState).get(toState).put(inputSymbol, stackSymbols);
    }

    // Getter for startState
    public String getStartState() {
        return startState;
    }

    // Setter for startState
    public void setStartState(String startState) {
        if (startState == null) {
            throw new IllegalArgumentException("Start State cannot be null");
        }
        this.startState = startState;
    }

    // Getter for startState
    public String getStartStackSymbol() {
        return startStackSymbol;
    }

    // Setter for startStackSymbol
    public void setStartStackSymbol(String startStackSymbol) {
        if (startStackSymbol == null) {
            throw new IllegalArgumentException("Start Stack Symbol cannot be null");
        }
        this.startStackSymbol = startStackSymbol;
    }
    
    // Getter for accepting states
    public Set<String> getAcceptingStates() {
        return acceptingStates;
    }

    // Setter for accepting states
    public void setAcceptingStates(Set<String> acceptingStates) {
        if (acceptingStates == null) {
            throw new IllegalArgumentException("Accepting States cannot be null");
        }
        this.acceptingStates = acceptingStates;
    }

    // Constructor
    public PushdownAutomaton(Set<String> states, Set<String> inputAlphabet, Set<String> stackAlphabet,
    Map<String, Map<String, Map<String, Set<String>>>> transitions,
    String startState, String startStackSymbol, Set<String> acceptingStates) {
        setStates(states);
        setInputAlphabet(inputAlphabet);
        setStackAlphabet(stackAlphabet);
        setTransitions(transitions);
        setStartState(startState);
        setStartStackSymbol(startStackSymbol);
        setAcceptingStates(acceptingStates);
    }
}
