package Interface;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class JoinWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtColor1;
	private JTextField txtColor2;

	private static JTextArea textPing = new JTextArea();
	private static JTextArea textrPlayers = new JTextArea();
	private static JTextArea textrInfo = new JTextArea();
	private String name;
	private InetAddress ip;
	private int port;

	private static Thread update;
	private static boolean runningU = true;

	public JoinWindow(String name) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JoinWindow frame = new JoinWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				update();
			}
		});
	}

	/**
	 * Create the frame.
	 */

	private static void update() {
		update = new Thread("Lobby Update") {

			public void run() {
				List<LobbyClient> clients = new ArrayList<LobbyClient>();

				while (runningU) {

					clients = JoinLobby.getClients();
					int ping = (int) JoinLobby.ping;

					String textP = "Players \n";
					
					for (int i = 0; i < clients.size(); i++) {
						LobbyClient c = clients.get(i);
						textP += c.name + "\t" + c.addr + "\t" + c.ping + " ms \n";
					}
					textP += JoinLobby.name + "\t local \t" + ping +" ms \n";
					textP += "\n" + (clients.size() + 1) + " players";
					textrPlayers.setText(textP);

					textPing.setText("Ping: \n " + ping + "ms");

					String textI = "Information: \n";
					try {
						textI += "IP Address: " + InetAddress.getLocalHost() + "\n";
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					textI += "Name: " + JoinLobby.name;
					textrInfo.setText(textI);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		};
		update.start();
	}

	public JoinWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JoinLobby.disconnect();
				runningU = false;
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(620, 414);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textrInfo.setText("Information\t\r\n\r\n");
		textrInfo.setRows(3);
		textrInfo.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		textrInfo.setEditable(false);
		textrInfo.setBackground(SystemColor.activeCaption);
		textrInfo.setBounds(10, 11, 390, 98);
		contentPane.add(textrInfo);

		textrPlayers.setText("Players");
		textrPlayers.setRows(9);
		textrPlayers.setFont(new Font("Century Gothic", Font.PLAIN, 17));
		textrPlayers.setEditable(false);
		textrPlayers.setBackground(SystemColor.activeCaption);
		textrPlayers.setBounds(10, 120, 390, 254);
		contentPane.add(textrPlayers);

		JLabel lblColor1 = new JLabel("Color of Tank Body:");
		lblColor1.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		lblColor1.setBounds(426, 204, 160, 14);
		contentPane.add(lblColor1);

		txtColor1 = new JTextField();
		txtColor1.setColumns(10);
		txtColor1.setBackground(SystemColor.activeCaption);
		txtColor1.setBounds(420, 221, 121, 28);
		contentPane.add(txtColor1);

		JLabel lblColor2 = new JLabel("Color of Turret:");
		lblColor2.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		lblColor2.setBounds(426, 270, 160, 14);
		contentPane.add(lblColor2);

		txtColor2 = new JTextField();
		txtColor2.setColumns(10);
		txtColor2.setBackground(SystemColor.activeCaption);
		txtColor2.setBounds(420, 286, 121, 28);
		contentPane.add(txtColor2);

		textPing.setText("Ping: ");
		textPing.setRows(2);
		textPing.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		textPing.setEditable(false);
		textPing.setBackground(SystemColor.activeCaption);
		textPing.setBounds(418, 52, 168, 57);
		contentPane.add(textPing);

		JLabel lblWaitingForHost = new JLabel("Waiting for Host...\r\n");
		lblWaitingForHost.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		lblWaitingForHost.setBounds(418, 12, 160, 24);
		contentPane.add(lblWaitingForHost);
	}
}
