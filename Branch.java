class Branch extends ParseTree{
  ParseTree leftNode;
  ParseTree rightNode;

  public Branch(ParseTree leftNode, ParseTree rightNode){
    this.leftNode = leftNode;
    this.rightNode = rightNode;
  }

  public Event getLeftNode(){
    return (Event)leftNode;
  }

  public ParseTree getRightNode(){
    return rightNode;
  }
}
