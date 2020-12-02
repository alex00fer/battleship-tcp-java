package socket;

import game.Tile;

public interface IConnection {
	
	public void startServer();
	public void startClient();
	
	public boolean shootSync(int x, int y);
	public void waitForOpponent(IShooted onShooted);
	
	public interface IShooted {
		public boolean shooted(int x, int y);
	}
	
	/*
	public void sendResult(Tile tile);
	
	public interface IResponse {
		
		public void shooted(int x, int y);
		public void response(Tile tile);
		
	}
	*/
}
