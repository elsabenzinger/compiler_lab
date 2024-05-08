//Vi kanske bara vill hantera allt som events och (kanske) siffror??? Idek
import java.util.ArrayList;

class Event extends ParseTree{
  TokenType tokenType;
  String data;
  ArrayList<ParseTree> repCommands;
  //För reps, datan är antalet repetitioner
  public Event(TokenType tokenType, String numReps, ArrayList<ParseTree> repCommands){
    this.tokenType = tokenType;
    this.data = numReps;
    this.repCommands = repCommands;
  }

  //För alla icke-slutsymboler som inte är up och down
  public Event(TokenType tokenType, String data) {
    this.tokenType = tokenType;
    this.data = data;
    //System.out.println("DATA saved: " + data);
  }


  //För up och down
  public Event(TokenType tokenType){
    this.tokenType = tokenType;
  }

  public TokenType getType(){
    return tokenType;
  }

  public String toString(){
    return "Tt: " + getType();
  }

  public String getData(){
    return data;
  }

  public ArrayList<ParseTree> getRepCommands(){
    return repCommands;
  }
}
