package app;

import game.Battleship;

public class Main {
	
	public static void main(String[] args) {
		Battleship.Configuration gameConfig = new Battleship.Configuration();
		gameConfig.setBoardSize(10);
		gameConfig.setNumberOfShips(10);
		
		Battleship battleship = new Battleship(gameConfig);
	}

}
