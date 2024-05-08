//Skrivet av Malin Svenberg och Elsa Benzinger
import java.util.Locale;
import java.util.ArrayList;

public class Translator{
  ParseTree tree;
  Event currentEvent;
  //Var pennan har för koordinater och var den tittar
  double x1;
  double y1;
  double x2;
  double y2;

  //Vänd mot (1,0), tänk enhetscirkeln
  double angle;

  String color;

  //Får vi ha detta här??
  boolean down;


  public Translator(ParseTree tree){
    this.tree = tree;
    Locale.setDefault(Locale.US);
    x1 = 0;
    y1 = 0;
    x2 = 0;
    y2 = 0;

    angle = 0;
    color  = "#0000FF";
    down = false;
  }


  public void translate(){
    //Gå igenom hela trädet tills vi hittar sista noden
    while(tree != null){
      if(tree instanceof Branch){
        currentEvent = ((Branch) tree).getLeftNode();
      } else {
        currentEvent = (Event) tree;
      }
      commands();
      if(tree instanceof Branch){
        tree = ((Branch) tree).getRightNode();
      } else{
        tree = null;
      }
    }
  }

  public void commands(){
    TokenType command = currentEvent.getType();
    switch(command){
      case Up:
        up();
        break;
      case Down:
        down();
        break;
      case Left:
        left();
        break;
      case Right:
        right();
        break;
      case Forw:
        forw();
        break;
      case Back:
        back();
        break;
      case Color:
        color();
        break;
      case Rep:
        rep();
        break;
    }
  }

  public void output(){
    String x1 = negativeZeroCheck(String.format("%.4f", this.x1));
    String y1 = negativeZeroCheck(String.format("%.4f", this.y1));
    String x2 = negativeZeroCheck(String.format("%.4f", this.x2));
    String y2 = negativeZeroCheck(String.format("%.4f", this.y2));
    // Kanske blir jätte långsamt, kanske behövs en Stringbuilder

    System.out.println(color.toUpperCase() + " " + x1 +  " " + y1 + " " + x2 + " " +  y2);
  }

  private String negativeZeroCheck(String number){
    if(number.equals("-0.0000")) return "0.0000";
    return number;
  }

  public void up(){
    down = false;
  }

  public void down(){
    down = true;
  }

  public void left(){
    angle = angle + Double.parseDouble(currentEvent.getData());
  }

  public void right(){
    angle = angle - Double.parseDouble(currentEvent.getData());
  }

  public void forw(){
    x1 = x2;
    y1 = y2;
    x2 = x2 + Double.parseDouble(currentEvent.getData()) * Math.cos(Math.toRadians(angle));
    y2 = y2 + Double.parseDouble(currentEvent.getData()) * Math.sin(Math.toRadians(angle));

    if(down){
      output();
    }
  }

  public void back(){
    x1 = x2;
    y1 = y2;
    x2 = x2 - Double.parseDouble(currentEvent.getData()) * Math.cos(Math.toRadians(angle));
    y2 = y2 - Double.parseDouble(currentEvent.getData()) * Math.sin(Math.toRadians(angle));


    if(down){
      output();
    }
  }

  public void color(){
    color = currentEvent.getData();
  }

  public void rep(){
    int numberReps = Integer.parseInt(currentEvent.getData());
    ArrayList<ParseTree> repCommands = currentEvent.getRepCommands();
    for(int i = 0; i < numberReps; i++){
      for( ParseTree comm : repCommands){
        currentEvent = ((Event) comm);
        commands();
      }
    }
  }
}
