import java.util.Map;
import java.util.Set;

public class ContextFreeGrammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<String, Set<String>> productions;
    private String startSymbol;

    // Getter for nonTerminals
    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    // Setter for nonTerminals
    public void setNonTerminals(Set<String> nonTerminals) {
        if (nonTerminals == null) {
            throw new IllegalArgumentException("Non-terminals cannot be null");
        }
        this.nonTerminals = nonTerminals;
    }

    // Getter for terminals
    public Set<String> getTerminals() {
        return terminals;
    }

    // Setter for terminals
    public void setTerminals(Set<String> terminals) {
         if (terminals == null) {
            throw new IllegalArgumentException("Terminals cannot be null");
        }
        this.terminals = terminals;
    }

    // Getter for productions
    public Map<String, Set<String>> getProductions() {
        return productions;
    }

    // Setter for productions
    public void setProductions(Map<String, Set<String>> productions) {
        if (productions == null) {
            throw new IllegalArgumentException("Productions cannot be null");
        }
        this.productions = productions;
    }

    // Getter for startSymbol
    public String getStartSymbol() {
        return startSymbol;
    }

    // Setter for startSymbol
    public void setStartSymbol(String startSymbol) {
        if (startSymbol == null) {
            throw new IllegalArgumentException("Start Symbol cannot be null");
        }
        this.startSymbol = startSymbol;
    }

    // Example constructor:
    public ContextFreeGrammar(Set<String> nonTerminals, Set<String> terminals,
                              Map<String, Set<String>> productions, String startSymbol) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }
}
