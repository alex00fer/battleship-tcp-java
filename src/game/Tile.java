package game;

public class Tile {
	
	private TileState state;

	public TileState getState() {
		return state;
	}

	public void setState(TileState state) {
		this.state = state;
	}
	
	public enum TileState { 
		UNKNOWN ('?'), 
		CLEAR(' '), 
		SHIP('O'), 
		SHOOTED_CLEAR('·'), 
		SHOOTED_SHIP('@');
		
		private final char representation;

		TileState(char representation){ 
	        this.representation = representation;
	    }
		
		public char getRepresentation() {
			return representation;
		}
	}
}
