package Interface;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinLobby implements Runnable {
	
	public static String name;
	public static InetAddress ip;
	public static int port;
	private long initTime = 0;
	public static byte ping = 0;
	public static String hostName;
	public static String hostAdd;
	private static DatagramSocket socket;
	private Thread run, receive;
	private static Thread send;
	boolean running = true;
	boolean connected = false;
	public static byte ID;
	public JoinWindow window;
	
	
	
	public static List<LobbyClient> clients = new ArrayList<LobbyClient>();
	
	public JoinLobby(String name, String address, int port) {

		JoinLobby.name = name;
		JoinLobby.port = port;
		JoinLobby.hostAdd = address;
		boolean error = false;
		try {
			socket = new DatagramSocket();
			this.ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			error = true;
			e.printStackTrace();
		} catch (SocketException e) {
			error = true;
			e.printStackTrace();
		}
		run = new Thread(this, "RunLobby");
		running = true;
		run.start();
		initTime = System.nanoTime();
		send("/c/" + name, ip, port);
	}

	public void run() {
		recieve();
	}

	private void recieve() {
		receive = new Thread("ReceivePlayer") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (Exception e) {
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}

	private void process(DatagramPacket packet) {
		String text = new String(packet.getData());
		
		if (text.startsWith("/c/")) {
			// connected
			deserializeInit(packet.getData());
			window = new JoinWindow(name);
			
			byte[] a = { "/".getBytes()[0], "c".getBytes()[0], "p".getBytes()[0], "/".getBytes()[0], ID, ping,
					"/".getBytes()[0], "e".getBytes()[0], "/".getBytes()[0] };
			send(a,ip,port);
			
			
		} else if (text.startsWith("/e/")) {
			System.out.println("Error");
		}

		else if (text.startsWith("/cc/")) {
			byte[] a = { "/".getBytes()[0], "c".getBytes()[0], "c".getBytes()[0], "/".getBytes()[0], ID,
					"/".getBytes()[0], "e".getBytes()[0], "/".getBytes()[0] };
			send(a,ip,port);
		}
		
		else if (text.startsWith("/nc/")){
			clients.add(deserializeClient(packet.getData()));
		}
		else if(text.startsWith("/cr/")){
			byte IDc = packet.getData()[4];
			for(int i =0; i < clients.size(); i++){
				if(clients.get(i).ID == IDc){
					clients.remove(i);
					break;
				}
			}
		}
		
		else{
			System.out.println("Unusual Text Recieved:  "  + text);
		}
	}
	
	private LobbyClient deserializeClient(byte[] data){
		String text = new String(data);
		if(!text.startsWith("/nc/")){
			System.out.println("Error att Deserialize Client, Join Lobby");
		}
		
		byte IDa = data[4];
		byte ping = data[5];
		String addr = new String(text.split("/aa/|/an/")[1]);
		String name = new String(text.split("/an/|/ee/")[1]);
		return new LobbyClient(IDa,name,addr,ping);
	}

	private void deserializeInit(byte[] data){
		ping = (byte)((double)(System.nanoTime() - initTime) / 1000000.0);	
		connected = true;
		
		int offset;
		String text = new String(data);
		hostName = text.split("/c/|/id/")[1];
		ID = data[text.indexOf("/id/") + 4];
		
		offset = text.indexOf("/nc/");
		
		do{
			if(offset>data.length) return;
			int index = text.indexOf("/nc/", offset + 4);
			if(index == -1) index = data.length;
			byte[] temp= Arrays.copyOfRange(data, offset, index);
			LobbyClient blah = deserializeClient(temp);
			clients.add(blah);
			
			offset = index;
			
		}while((text.indexOf("/nc/", offset) != -1));
	}
	
	
	private static void send(final byte[] data, final InetAddress ip, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void send(String text, InetAddress ip, int port) {
		String data = text + "/e/";
		send(data.getBytes(), ip, port);
	}
	
	public static void disconnect(){
		byte[] a = { "/".getBytes()[0], "d".getBytes()[0], "/".getBytes()[0], ID,
				"/".getBytes()[0], "e".getBytes()[0], "/".getBytes()[0] };
		send(a,ip,port);
	}
	
	
	public static List<LobbyClient> getClients(){
		return clients;
	}
	public static void startGame(){
		
	}
}


