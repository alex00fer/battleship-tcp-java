package game;

public class Battleship {
	
	public static class Configuration {
		private int boardSize;
		private int numberOfShips;
		
		public void setBoardSize(int boardSize) {
			this.boardSize = boardSize;
		}
		public void setNumberOfShips(int numberOfShips) {
			this.numberOfShips = numberOfShips;
		}
	}
	
	private int boardSize;
	private int numberOfShips;
	
	public Battleship (Configuration gameConfig) {
		this.boardSize = gameConfig.boardSize;
		this.numberOfShips = gameConfig.numberOfShips;
	}

}
