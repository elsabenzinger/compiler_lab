//Written by Elsa Benzinger and Malin Svenberg
import java.util.ArrayList;

public class Parser{
  private Lexer lexer;
  public String str;

  public Parser(Lexer lexer) {
		this.lexer = lexer;
    this.str = "";

	}

  public ParseTree parse() throws SyntaxError {
    if(lexer.isEmpty()) return null; //TODO kanske inte behövs
    ParseTree result = command();

    if (lexer.hasMoreTokens()){
      //TODO: Radnummer
  			throw new SyntaxError(lexer.nextToken().getRowNumber());
    }
    return result;
	}

  /* Only for reference
  public enum TokenType {
  	Forw, Back, Left, Right, Up, Down, Color, Rep, Period, Quote, Decimal, Hex,
    Error
  }
  */

  public ParseTree command() throws SyntaxError{
    //Nu sparas allt somm comm(tokentype, data, nextComm) eller comm(tokentype, nextComm)
    //om det är up/down. Sen blir det upp till när vi exekverar det vi får från syntaxträdet
    //att ignorera up-delar den kan köras som en while ParseTree.tokenType != null typ
    /**if(!lexer.hasMoreTokens()){
      return new Event(null, null);
    } else{*/
    ParseTree event = whichCommand();
    TokenType tt = null;
    if(lexer.peekToken() != null) tt = lexer.peekToken().getType();
    if((tt == TokenType.Forw) || (tt == TokenType.Back) || (tt == TokenType.Left) || (tt == TokenType.Right) || (tt == TokenType.Up) || (tt == TokenType.Down)|| (tt == TokenType.Color) || (tt == TokenType.Rep)){
      lexer.nextToken();
      return new Branch(event, command());
    }
    //TODO: tillåter vad som helst som sista rad.
    return event;

    //}
  }

  public ParseTree whichCommand() throws SyntaxError{
    Event result;
    TokenType tt = lexer.getCurrentToken().getType();
    //System.out.println(tt);
    if(tt == TokenType.Forw){
       result = forw();
    }else if (tt == TokenType.Back){
      result = back();
    }else if (tt == TokenType.Left){
      result = left();
    }else if (tt == TokenType.Right){
      result = right();
    }else if (tt == TokenType.Up){
      result = up();
    }else if (tt == TokenType.Down){
      result = down();
    }else if (tt == TokenType.Color){
      result = color();
    } else if (tt == TokenType.Rep){
      result = rep();
    } else{
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }

    return result; // vill vi retunera detta?
  }

  public Event forw() throws SyntaxError{
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    Token token2 = lexer.nextToken();
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    if(token2.getType() == TokenType.Decimal && lexer.nextToken().getType() == TokenType.Period){
      //System.out.println("Forw");
      return new Event(TokenType.Forw, token2.getData()); //syntaxträd
    } else {
      //System.out.println(lexer.getCurrentToken().getType());
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

  public Event back() throws SyntaxError{
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    Token token2 = lexer.nextToken();
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    if(token2.getType() == TokenType.Decimal && lexer.nextToken().getType() == TokenType.Period){
      return new Event(TokenType.Back, token2.getData()); //syntaxträd
    } else {
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

  public Event right() throws SyntaxError{
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    Token token2 = lexer.nextToken();
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    if(token2.getType() == TokenType.Decimal && lexer.nextToken().getType() == TokenType.Period){
      return new Event(TokenType.Right, token2.getData()); //syntaxträd
    } else {
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

  public Event left() throws SyntaxError{
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    Token token2 = lexer.nextToken();
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    if(token2.getType() == TokenType.Decimal && lexer.nextToken().getType() == TokenType.Period){
      return new Event(TokenType.Left, token2.getData());
    } else {
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

  public Event down() throws SyntaxError{
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    if(lexer.nextToken().getType() == TokenType.Period){
      return new Event(TokenType.Down); //syntaxträd
      }
    else {
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

  public Event up() throws SyntaxError{
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    if(lexer.nextToken().getType() == TokenType.Period){
        return new Event(TokenType.Up);
    }
    else {
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

  public Event color() throws SyntaxError{
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    Token token2 = lexer.nextToken();
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    if(token2.getType() == TokenType.Hex && lexer.nextToken().getType() == TokenType.Period){
      return new Event(TokenType.Color, token2.getData());
    }
    else {
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

/*
  public ParseTree quote() throws SyntaxError{
      return null; //testa detta annars retunera en nod?
  }*/


//TODO: Lista ur vad vi ska göra här
//Vi kanske vill ha den först typ på nåt sätt? Så att vi kan
  public Event rep() throws SyntaxError {
    if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    Token token2 = lexer.nextToken();
    if(token2.getType() == TokenType.Decimal){
      ArrayList<ParseTree> repCommands = new ArrayList<>();
      if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
      if(lexer.nextToken().getType() == TokenType.Quote){
        if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
        Token comm = lexer.nextToken();
        while(comm.getType() != TokenType.Quote){
          repCommands.add(whichCommand());
          if(lexer.peekToken() == null) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
          comm = lexer.nextToken();
        }
        if(repCommands.size() == 0) throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
        return new Event(TokenType.Rep, token2.getData(), repCommands);
      } else {
          repCommands.add(whichCommand());
          return new Event(TokenType.Rep, token2.getData(), repCommands);
      }
    } else {
      throw new SyntaxError(lexer.getCurrentToken().getRowNumber());
    }
  }

}
