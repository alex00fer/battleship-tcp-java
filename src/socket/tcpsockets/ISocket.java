package socket.tcpsockets;

public interface ISocket {
	
	public void sendLineSync(String line);
	public String receiveLineSync(String line);
	public void close();

}
