package Interface;

public class LobbyClient{
	public String name;
	public String addr;
	public byte ping;
	public byte ID;
	
	public LobbyClient(byte ID, String name, String addr, byte ping){
		this.ID =ID;
		this.name = name;
		this.addr = addr;
		this.ping = ping;
	}
}
