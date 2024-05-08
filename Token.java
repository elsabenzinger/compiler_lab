//Written by Elsa Benzinger and Malin Svenberg
//Inspo från BinTreeParser av Per Austrin


public class Token{
	private TokenType tokenType;
	private String data;
	private int rowNumber;

	public Token(TokenType tokenType, String data, int rowNumber){
		this.tokenType = tokenType;
		this.data = data;
		this.rowNumber = rowNumber;
	}

	public Token(TokenType tokenType, int rowNumber){
		this.rowNumber = rowNumber;
		this.tokenType = tokenType;
	}


	public TokenType getType(){
		return tokenType;
	}

	public String getData(){
		return data;
	}

	public int getRowNumber(){
		return rowNumber;
	}

	public String toString(){
		switch (tokenType){
			case Forw:
				return "forw";
			case Back:
				return "back";
			case Left:
				return "left";
			case Right:
				return "right";
			case Up:
				return "up";
			case Down:
				return "down";
			case Color:
				return "color";
			case Rep:
				return "rep";
			case Period:
				return "period";
			case Quote:
				return "quote";
			case Decimal:
				return "decimal";
			case Hex:
				return "hex";
			case Error:
				return "error";
			default:
				return "something went wrong";
		}
	}
}
