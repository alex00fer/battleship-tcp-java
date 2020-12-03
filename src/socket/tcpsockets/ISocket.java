package socket.tcpsockets;

public interface ISocket {
	
	public void sendLineSync(String line);
	public String receiveLineSync();
	public void close();

}
