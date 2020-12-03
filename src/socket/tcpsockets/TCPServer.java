package socket.tcpsockets;

import java.io.*;
import java.net.*;

public class TCPServer implements ISocket {
	
	private ServerSocket server;
	private Socket client;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public TCPServer(int port) throws UnknownHostException, IOException {
		try {
			server = new ServerSocket(port);
			System.out.println("Esperando conexión...");
			client = server.accept();
			System.out.println("Conexión acceptada: " + client);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
		} catch (IOException e) {
			System.out.println("No puede escuchar en el puerto: " + port);
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
	
	public String receiveLineSync(String line) {
		String msg = null;
		try {
			msg = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

}
