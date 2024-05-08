//Written by Elsa Benzinger and Malin Svenberg
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File; //TODO: tabort
import java.io.FileInputStream; //TODO: tabort
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/*
public enum TokenType {
	Forw, Back, Left, Right, Up, Down, Color, Rep, Period, Quote, Decimal, Hex,
  Error
}
*/
//TODO: Vi kan få problem om whitespace mellan command och decimal är ny rad.
public class Lexer {

  private String input;
  private List<Token> tokens;
  private int currentToken;
  int numReps;
  int rowNumber = 1;
  private String regex = "(%.*)(\\n|\\Z)|(\\s+)|(forw)(\\s+|(?=%))|(back)(\\s+|(?=%))|(left)(\\s+|(?=%))|(right)(\\s+|(?=%))|down\\s*|up\\s*|(color)(\\s+|(?=%))|(rep)(\\s+|(?=%))|(?<!,)[1-9][0-9]*(?=\\.|\\s|%)\\s*|#([0-9a-fA-F]){6}|\\s*\\.\\h*|\"\\h*";
  //private String regex = "(%.*)(\\n\\s*|\\Z)|(\\n+)|(forw)|(back)|(left)|(right)|(down)|(up)|(color)|(rep)|\\d+|#([0-9a-fA-F]){6}|\\.|\"";

  public Lexer(InputStream in) throws java.io.IOException {
    //System.out.println("Started");
    input = Lexer.readInput(in);
    //System.out.println("readInput: " + input);
    Pattern tokenPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    //System.out.println("tokenPattern");
    Matcher matches = tokenPattern.matcher(input);
    //System.out.println("matches: " + matches);
    int inputPos = 0;
    tokens = new ArrayList<Token>();
    currentToken = 0;

    while(matches.find()){



      if(matches.start() != inputPos){
        //System.out.println(matches.start() + "  " + inputPos);
        //System.out.println("Error occured");
        //System.out.println(rowNumber);
        tokens.add(new Token(TokenType.Error, rowNumber));
      }




      //String lines[] = matchGroup.split("\\r?\\n", -1);
      //System.out.println(lines);
      String matchGroup = matches.group().toLowerCase();
      // System.out.println("mg är: " + matchGroup);

      if(matchGroup.matches("(%.*)(\\n|\\Z)")){
        //System.out.println("Det här är en kommentar!");
        //System.out.println("End of commentar " + matches.end());
        //System.out.println("On row: " + rowNumber);
        //Eat all characters in comment
      }
      else if( matchGroup.matches("\\s+")){
        // System.out.println("hejhej");
      }
      else if (matchGroup.matches("(forw)\\s*")) { //TODO: test if works with upper- and lowercase letters
        tokens.add(new Token(TokenType.Forw, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if (matchGroup.matches("(back)\\s*")){
        tokens.add(new Token(TokenType.Back, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if (matchGroup.matches("(left)\\s*")){
        tokens.add(new Token(TokenType.Left, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if (matchGroup.matches("(right)\\s*")){
        tokens.add(new Token(TokenType.Right, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if (matchGroup.matches("down\\s*")){
        tokens.add(new Token(TokenType.Down, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if (matchGroup.matches("up\\s*")){
        tokens.add(new Token(TokenType.Up, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if(matchGroup.matches("color\\s*")){
        tokens.add(new Token(TokenType.Color, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if(matchGroup.matches("(rep)\\s*")){
        tokens.add(new Token(TokenType.Rep, rowNumber));
      //  System.out.println("On row: " + rowNumber);
      } else if(matchGroup.matches("\\s*\\.\\s*")){
        tokens.add(new Token(TokenType.Period, rowNumber));
        //System.out.println("On row: " + rowNumber);
      } else if(matchGroup.matches("\"\\s*")){
        tokens.add(new Token(TokenType.Quote, rowNumber));
      //  System.out.println("On row: " + rowNumber);
      } else if(matchGroup.matches("#([0-9a-fA-F]){6}")){
        tokens.add(new Token(TokenType.Hex, matchGroup, rowNumber));
      //  System.out.println("On row: " + rowNumber);
      } else if (Character.isDigit(matchGroup.trim().charAt(0))){ //TODO: Vi matchar 34 i 5AB34 input 3., matcher 3 i 2,3 input 5.
        String data = matchGroup.trim();
        tokens.add(new Token(TokenType.Decimal, data, rowNumber));
      //  System.out.println("On row: " + rowNumber);
      }

      if(matchGroup.matches(".*(\\s+).*")){
        //System.out.println("New Row");

        rowNumber += matchGroup.split("\\n", -1).length - 1;
        /*String[] test = matchGroup.split("\\n", -1);
        String str = "[";
        for(String s : test){
          str += s + ",";
          }
          str += "]";
        System.out.println(str);
        System.out.println(rowNumber);
        */
      }

      numReps++;
      //System.out.println(numReps);
      inputPos = matches.end();

    }

    if(inputPos != input.length()){
      //System.out.println("Ko?");
      tokens.add(new Token(TokenType.Error, rowNumber));
    }

   //System.out.println(tokens.toString());

  //  System.out.println("Done");
  }

  private static String readInput(InputStream in) throws java.io.IOException{
    //File initialFile = new File("input.txt");
    //in = new FileInputStream(initialFile);

    Reader reader = new InputStreamReader(in);
    StringBuilder sb = new StringBuilder();
    char cList[] = new char[1024];
    //System.out.println("Currently Reading input");
    //int current = reader.read(cList);
    int current = 0;
    //int loop = 1;
    while((current = reader.read(cList)) != -1){ //quit with ^, TODO: change back to -1
      sb.append(cList, 0, current);
    }

    //sb.append(cList, 0, current);
    return sb.toString();
  }

	// Hämta nästa token i indata och gå framåt i indata
  // TODO: ha ett faktiskt radnummer och inte bara 0
	public Token nextToken() throws SyntaxError{
    if (!hasMoreTokens()){
      return null;
    }
    currentToken++;
    //System.out.println("Current token: " + currentToken);
		return tokens.get(currentToken);
	}

  public boolean hasMoreTokens(){
    return currentToken+1 < tokens.size();
  }

  public boolean isEmpty(){// TODO: kanske inte behövs
    return !(tokens.size() > 0);
  }

  public int getTokenIndex(){
    return currentToken;
  }

  public Token getCurrentToken(){
    //System.out.println("Current token: " + currentToken);
    return tokens.get(currentToken);
  }

  public Token peekToken(){
    if(!hasMoreTokens()) return null;
    return tokens.get(currentToken + 1);
  }
}
