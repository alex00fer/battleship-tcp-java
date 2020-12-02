package game;

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
	
	public enum ConnectionMode { CLIENT, SERVER }
	//private ConnectionMode connectionMode;
	
	public Battleship (Configuration gameConfig, IConnection conn) {
		this.boardSize = gameConfig.boardSize;
		this.numberOfShips = gameConfig.numberOfShips;
		this.conn = conn;
	}
	
	public void start(ConnectionMode mode) {
		//connectionMode = mode;
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
				switch (board.shooted(x, y)) {
				case SHOOTED_CLEAR:
					return false;
				case SHOOTED_SHIP:
					return true;	
				default:
					return false;
				}
			}
		};
		
		if (!shootFirst) { // opponent shoots first, wait for it
			conn.waitForOpponent(onShooted); 
		}
		
		while (running) {
			
			System.out.println("Enter attack tile:");
			conn.shootSync(0, 0); // shoot
			conn.waitForOpponent(onShooted);  // wait
			
		}
		
	}

}
