package socket;

import socket.tcpsockets.ISocket;
import socket.tcpsockets.TCPClient;
import socket.tcpsockets.TCPServer;

public class TcpGameConnection implements IConnection {

	private String address;
	private int port;
	private ISocket socket;

	public TcpGameConnection(String address, int port) {
		this.address = address;
		this.port = port;
	}

	@Override
	public void startServer() {
		socket = new TCPServer(port);
	}

	@Override
	public void startClient() {
		socket = new TCPClient(address, port);
	}

	@Override
	public boolean shootSync(int x, int y) {
		socket.sendLineSync(String.format("%s;%d;%d", "SHOOT", x, y));
		String[] response = socket.receiveLineSync().split(";");
		assertResponse(response[0], "RESULT");
		assertLength(response.length, 2);
		return response[1].equalsIgnoreCase("TRUE") ? true : false;
	}

	@Override
	public void waitForOpponent(IShooted onShooted) {
		String[] response = socket.receiveLineSync().split(";");
		assertResponse(response[0], "SHOOT");
		assertLength(response.length, 3);
		int x = Integer.parseInt(response[1]);
		int y = Integer.parseInt(response[2]);
		boolean result = onShooted.shooted(x, y);
		socket.sendLineSync(String.format("%s;%s", "RESULT", result ? "TRUE" : "FALSE"));
	}

	private void assertResponse(String response, String expected) {
		if (!response.equalsIgnoreCase(expected))
			throw new RuntimeException("Unexpected response. "
					+ String.format("%s was expected but instead received %s", expected, response));
	}
	
	private void assertLength(int response, int expected) {
		if (response != expected)
			throw new RuntimeException("Unexpected number of parameters. "
					+ String.format("%d was expected but instead received %d", expected, response));
	}

}
