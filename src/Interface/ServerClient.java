package Interface;

import java.net.InetAddress;

public class ServerClient {
	public String name;
	public InetAddress ip;
	public int port;
	public byte ID;
	public int attempt =0;
	public final int MAX_ATTEMPT = 5;
	public byte ping=0;

	public ServerClient(String name, InetAddress ip, int port, byte ID) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.ID = ID;
	}
	
	private static byte count =0;
	public static byte generateID() {
		return count++;
	}
}
