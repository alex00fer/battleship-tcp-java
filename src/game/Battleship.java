package game;

import app.ScannerWrapper;
import game.Tile.TileState;
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
	private boolean running = false;

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

		running = true;

		IShooted onShooted = new IConnection.IShooted() {
			@Override
			public boolean shooted(int x, int y) {
				TileState tileState = board.shooted(x, y);

				boolean hit = false;
				switch (tileState) {
				case SHOOTED_CLEAR:
					hit = false;
				case SHOOTED_SHIP:
					hit = true;
				default:
					hit = false;
				}
				
				System.out.println(
						String.format("You have been shooted at [%d,%d] | %s", x, y, 
								hit ? "A ship was destroyed!" : "It hit the water"));
				
				return hit;
			}
		};

		if (!shootFirst) { // opponent shoots first, wait for it
			System.out.println(" Waiting for opponent...");
			conn.waitForOpponent(onShooted);
		}

		while (running) {
			// TODO show boards

			System.out.println(" Enter attack tile:");
			boolean result = conn.shootSync( // shoot
					ScannerWrapper.readInteger("   > X", 0, boardSize - 1),
					ScannerWrapper.readInteger("   > Y", 0, boardSize - 1));
			System.out.println(result ? " You destroyed an enemy ship!" : " It hit the water");
			System.out.println(" Waiting for opponent's action...");
			
			conn.waitForOpponent(onShooted); // wait

		}

	}

}
