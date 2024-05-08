//Written by Elsa Benzinger and Malin Svenberg

public class SyntaxError extends Exception {
    public SyntaxError(int r) {
       System.out.println("Syntaxfel på rad " + r);
    }
}
