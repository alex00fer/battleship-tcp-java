package game;

import app.ScannerWrapper;
import socket.IConnection;
import socket.IConnection.IShooted;

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
	private Board board;
	private IConnection conn;

	public enum ConnectionMode {
		CLIENT, SERVER
	}

	public Battleship(Configuration gameConfig, IConnection conn) {
		this.boardSize = gameConfig.boardSize;
		this.numberOfShips = gameConfig.numberOfShips;
		this.conn = conn;
	}

	public void start(ConnectionMode mode) {
		board = new Board(boardSize, numberOfShips);

		switch (mode) {
		case CLIENT:
			conn.startClient();
			break;
		case SERVER:
			conn.startServer();
			break;
		}

		// server shoots first
		boolean shootsFirst = mode == ConnectionMode.SERVER ? true : false;
		gameLoop(shootsFirst);
	}

	private void gameLoop(boolean shootFirst) {

		boolean winner = false;

		IShooted onShooted = new IConnection.IShooted() {
			@Override
			public boolean shooted(int x, int y) {
				boolean hit = board.shooted(x, y);

				System.out.println(String.format(" You have been shooted at [%d,%d] | %s", x, y,
						hit ? "A ship was destroyed!" : "It hit the water"));

				return hit;
			}
			
			@Override
			public boolean isGameOver() {
				return board.haveILost();
			}
		};

		if (!shootFirst) { // opponent shoots first, wait for it
			board.print(); //show board
			System.out.println(" Waiting for opponent's action...");
			conn.waitForOpponent(onShooted);
		}

		while (!board.haveILost()) {
			board.print(); //show board

			System.out.println(" Enter attack tile:");
			int x = ScannerWrapper.readInteger("   > X", 0, boardSize - 1);
			int y = ScannerWrapper.readInteger("   > Y", 0, boardSize - 1);
			boolean result = conn.shootSync(x, y); // shoot

			System.out.println(result ? " You destroyed an enemy ship!" : " It hit the water");
			board.updateEnemyTile(x, y, result);
			
			if (conn.opponentLost()) { // You win
				winner = true;
				break; 
			}

			System.out.println(" Waiting for opponent's action...");
			conn.waitForOpponent(onShooted); // wait

		}
		
		System.out.println("     * * *     ");
		if (winner)
			System.out.println(" YOU WON THE GAME!");
		else
			System.out.println(" YOU LOST... YOUR ENTIRE FLEET WAS DESTROYED");

	}

}
