package app;

import game.Battleship;
import game.Battleship.ConnectionMode;
import socket.IConnection;
import socket.TcpGameConnection;

public class Main {
	
	public static void main(String[] args) {
		Battleship.Configuration gameConfig = new Battleship.Configuration();
		gameConfig.setBoardSize(10);
		gameConfig.setNumberOfShips(10);
		
		IConnection conn = new TcpGameConnection("localhost", 45322);
		Battleship battleship = new Battleship(gameConfig, conn);
		battleship.start(askConnectionMode());
	}
	
	private static ConnectionMode askConnectionMode() {
		if (ScannerWrapper.readString("Start as server? (NO/yes)", 0, 3).equalsIgnoreCase("yes")) {
			return ConnectionMode.SERVER;
		} else {
			return ConnectionMode.CLIENT;
		}
	}

}
