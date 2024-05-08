//Written by Elsa Benzinger and Malin Svenberg
public class Main {
  public static void main(String[] args) throws java.io.IOException, SyntaxError{
      try{
        Lexer lexer = new Lexer(System.in);
        Parser parser = new Parser(lexer);
        ParseTree program = parser.parse();
        Translator trs = new Translator(program);
        trs.translate();
      }
      catch( SyntaxError s){
      }
  }
}
