package socket.tcpsockets;

import java.io.*;
import java.net.*;

public class TCPClient implements ISocket {
	
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public TCPClient(String address, int port) {
		try {
			socket = new Socket(address, port);
			System.out.println("Connection stablished: " + socket.getInetAddress());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			System.err.printf("Cannot connect to server %s:%d \n",address,port);
			System.err.println("Terminating game");
			System.exit(-1);
		}
	}
	
	public void close() {
		try {
			writer.close();
			reader.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client closed");
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
