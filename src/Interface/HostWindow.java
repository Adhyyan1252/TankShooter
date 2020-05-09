package Interface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.TextArea;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.awt.event.ActionEvent;

public class HostWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtCol1;
	private JTextField txtCol2;
	private static String name;
	private static int port;
	
	private static JTextArea txtrPlayers = new JTextArea();
	private static JTextArea txtrInfo = new JTextArea();
	
	private static ServerLobby lobby;
	/**
	 * Launch the application.
	 */
	public HostWindow(String name, int port){
		this.name = name;
		this.port = port;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HostWindow frame = new HostWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				lobby = new ServerLobby(name, port);
				update();
				
			}
		});
	}
	
	private static void update(){
		boolean runningUpdate = true;
		new Thread("Update Window"){
			public void run(){
				
				String textP = "", textI = "";
				List<ServerClient> clients;
				while(runningUpdate){
					
					clients = lobby.getClients();
					textP = "Players \n";
					textP+= name + "\t Host \n";
					for(int i =0; i< clients.size();i++){
						ServerClient c = clients.get(i);
						textP+= c.name + "\t" + c.ip.getHostAddress() + "\t" + c.ping+ " ms \n";
					}
					textP+= "\n" + (clients.size() + 1) + " players";
					txtrPlayers.setText(textP);
					
					
					
					textI = "Information: \n";
					try {
						textI += "IP Address: " + InetAddress.getLocalHost() + "\n";
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					textI += "Port: " + port;  
					txtrInfo.setText(textI);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}

	/**
	 * Create the frame.
	 */
	public HostWindow() {
		setTitle("Host Game");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(620, 414);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.controlHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("Start Game");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Start Game
				ServerLobby.startGame();
			}
		});
		btnStart.setBackground(Color.GRAY);
		btnStart.setFont(new Font("Century Gothic", Font.BOLD, 16));
		btnStart.setBounds(450, 23, 138, 68);
		contentPane.add(btnStart);
		
		
		txtrPlayers.setBackground(SystemColor.activeCaption);
		txtrPlayers.setFont(new Font("Century Gothic", Font.PLAIN, 17));
		txtrPlayers.setText("Players");
		txtrPlayers.setRows(9);
		txtrPlayers.setEditable(false);
		txtrPlayers.setBounds(28, 120, 390, 254);
		contentPane.add(txtrPlayers);
		txtrPlayers.setText("Players \n");
		
		
		txtrInfo.setEditable(false);
		txtrInfo.setBackground(SystemColor.activeCaption);
		txtrInfo.setText("Information\t\r\n\r\n");
		txtrInfo.setRows(3);
		txtrInfo.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtrInfo.setBounds(28, 11, 390, 98);
		contentPane.add(txtrInfo);
		
		JTextArea txtrName = new JTextArea();
		txtrName.setText("Name:");
		txtrName.setRows(2);
		txtrName.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtrName.setEditable(false);
		txtrName.setBackground(SystemColor.activeCaption);
		txtrName.setBounds(436, 121, 168, 57);
		txtrName.setText("Name: \n" + name);
		contentPane.add(txtrName);
		
		txtCol1 = new JTextField();
		txtCol1.setBackground(SystemColor.activeCaption);
		txtCol1.setBounds(438, 221, 121, 28);
		contentPane.add(txtCol1);
		txtCol1.setColumns(10);
		
		JLabel lblColorOfTank = new JLabel("Color of Tank Body:");
		lblColorOfTank.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		lblColorOfTank.setBounds(444, 204, 160, 14);
		contentPane.add(lblColorOfTank);
		
		JLabel lblColorOfTurret = new JLabel("Color of Turret:");
		lblColorOfTurret.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		lblColorOfTurret.setBounds(444, 270, 160, 14);
		contentPane.add(lblColorOfTurret);
		
		txtCol2 = new JTextField();
		txtCol2.setColumns(10);
		txtCol2.setBackground(SystemColor.activeCaption);
		txtCol2.setBounds(438, 286, 121, 28);
		contentPane.add(txtCol2);
	}
}
