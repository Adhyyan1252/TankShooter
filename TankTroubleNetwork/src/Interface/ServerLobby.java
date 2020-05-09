package Interface;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ServerLobby implements Runnable {
	private static List<ServerClient> clients = new ArrayList<ServerClient>();
	private static DatagramSocket socket;
	private int port;
	private int color1, color2;

	private Thread run, manage;
	private static Thread send;
	private Thread receive;
	private boolean running = false;
	private byte hostID;
	private String hostName;
	private int hostPort;

	public ServerLobby(String name, int port) {
		this.port = port;
		this.hostName = name;

		this.hostPort = port;

		hostID = ServerClient.generateID();
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();

	}

	public void run() {
		running = true;
		recieve();
		manageClients();
	}

	private void manageClients() {
		manage = new Thread("Manage Clients") {
			public void run() {
				int i = 0;
				while (running) {
					long time;
					try {
						time = (long) (2000) / (long) (clients.size());
					} catch (ArithmeticException e) {
						time = 1000;
					}
					try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (i >= clients.size())
						i = 0;
					if (clients.size() != 0) {
						ServerClient c = clients.get(i);
						if (c.attempt >= c.MAX_ATTEMPT) {
							disconnect(i);
						} else {
							send("/cc/", c.ip, c.port);
							c.attempt++;
						}
					}
					i++;
				}
			}
		};
		manage.start();
	}

	private void recieve() {
		receive = new Thread("RecieveServer") {
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

	private static void send(final byte[][] data, final InetAddress ip, final int port) {
		byte[] sendData = addBytes(data);
		send(sendData, ip, port);
	}

	private void send(String text, InetAddress ip, int port) {
		String data = text + "/e/";
		send(data.getBytes(), ip, port);
	}

	private void process(DatagramPacket packet) {
		String text = new String(packet.getData());

		if (text.startsWith("/c/")) { // Initial Connection

			String name = text.split("/c/|/e/")[1];
			byte ID = ServerClient.generateID();
			clients.add(new ServerClient(name, packet.getAddress(), packet.getPort(), ID));

			byte[] sendData = serializeAll(ID);
			System.out.println(new String(sendData));
			send(sendData, packet.getAddress(), packet.getPort());
		}

		else if (text.startsWith("/d/")) {// Disconnect
			byte IDa = packet.getData()[3];
			disconnect(IDa);
		}

		else if (text.startsWith("/cc/")) {// Check if still connected
			byte IDb = packet.getData()[4];
			clients.get(getIndex(IDb)).attempt = 0;
		}

		else if (text.startsWith("/cp/")) {// receive ping
			byte IDc = packet.getData()[4];
			ServerClient blah = clients.get(getIndex(IDc));
			blah.ping = packet.getData()[5];

			byte[] data = serializeClient(blah, true);

			for (int x = 0; x < clients.size(); x++) {
				if (clients.get(x).ID == blah.ID)
					continue;
				send(data, clients.get(x).ip, clients.get(x).port);
			}
		}
	}

	private byte[] serializeClient(ServerClient blah, boolean iftrue) {
		byte[][] data = { "/nc/".getBytes(), new byte[] { blah.ID, blah.ping }, "/aa/".getBytes(),
				blah.ip.getHostAddress().getBytes(), "/an/".getBytes(), blah.name.getBytes(), "/ee/".getBytes(),
				iftrue ? "/e/".getBytes() : "".getBytes() };

		byte[] Data = addBytes(data);
		return Data;
	}

	private byte[] serializeAll(byte ID) {
		byte[][] data = new byte[clients.size() + 10][];
		data[0] = "/c/".getBytes();
		data[1] = hostName.getBytes();
		data[2] = "/id/".getBytes();
		data[3] = new byte[] { ID };
		data[4] = "/mnc//nc/".getBytes();
		data[5] = new byte[] { hostID, 0 };
		data[6] = "/aa/Host/an/".getBytes();
		data[7] = hostName.getBytes();
		data[8] = "/ee/".getBytes();

		for (int i = 0; i < clients.size(); i++) {
			ServerClient x = clients.get(i);
			if (x.ID == ID)
				continue;
			data[i + 8] = serializeClient(x, false);
		}
		data[data.length - 1] = "/e/".getBytes();
		byte[] Data = addBytes(data);
		return Data;
	}

	private int getIndex(byte ID) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).ID == ID)
				return i;
		}
		return -5;
	}

	private void disconnect(byte ID) {
		disconnect(getIndex(ID));
	}

	private void disconnect(int index) {
		System.out.println("Disconnected " + clients.get(index).name);
		byte IDc = clients.get(index).ID;
		clients.remove(index);
		byte[][] data = new byte[10][];
		data[0] = "/cr/".getBytes();
		data[1] = new byte[] { IDc };
		data[2] = "/e/".getBytes();

		for (int x = 0; x < clients.size(); x++) {
			send(data, clients.get(x).ip, clients.get(x).port);
		}
	}

	public void closeServer() {
		for (int i = 0; i < clients.size(); i++) {
			clients.remove(i);
		}
		socket.close();
	}

	public List<ServerClient> getClients() {
		return clients;
	}

	private static byte[] addBytes(final byte[][] smallArrays) {
		int currentOffset = 0;
		byte[] dest = new byte[1024];
		for (final byte[] currentArray : smallArrays) {
			if (currentArray == null)
				continue;
			System.arraycopy(currentArray, 0, dest, currentOffset, currentArray.length);
			//
			currentOffset += currentArray.length;
		}
		byte[] data = new byte[currentOffset];
		for (int i = 0; i < data.length; i++) {
			data[i] = dest[i];
		}
		return data;
	}

	public static long gameInitTime;

	public static void startGame() {
		gameInitTime = System.currentTimeMillis() + 2000;
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(gameInitTime);

		byte[][] msg = { "/s/".getBytes(), buffer.array(), "/e/".getBytes() };
		for (int i = 0; i < clients.size(); i++) {
			send(msg,clients.get(i).ip, clients.get(i).port);
		}
	}
}
