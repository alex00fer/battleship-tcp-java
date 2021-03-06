package socket.tcpsockets;

import java.io.*;
import java.net.*;

public class TCPServer implements ISocket {
	
	private ServerSocket server;
	private Socket client;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public TCPServer(int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("Waiting for an opponent...");
			client = server.accept();
			System.out.println("Opponent connected: " + client.getInetAddress());
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
		} catch (IOException e) {
			System.err.println("Server cannot start at port: " + port + 
					". This usually means that the port is already in use.");
			System.exit(-1);
		}
	}

	public void close() {
		try {
			writer.close();
			reader.close();
			client.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");
	}

	public void sendLineSync(String line) {
		writer.println(line);
	}

	public String receiveLineSync() {
		String msg = null;
		try {
			msg = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

}
