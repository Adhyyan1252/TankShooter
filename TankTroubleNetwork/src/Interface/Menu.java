package Interface;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.game.Start;
import java.awt.Color;

public class Menu extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtPort;
	private JTextField txtAdd;
	private JTextField txtPortJ;

	static JLabel lblError = new JLabel("Error! Couldn't join game.\r\n\r\n");
	
	private Start game;
	private String name, address;
	private int port;
	private int color1, color2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Menu() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setTitle("Tank Trouble Menu");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 370);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(57, 87, 180, 26);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(121, 70, 46, 14);
		contentPane.add(lblName);
		
		JButton btnPlayLocal = new JButton("Play Local\r\n\r\n\r\n\r\n\r\n");
		btnPlayLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				game = new Start();
			}
		});
		btnPlayLocal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnPlayLocal.setBounds(85, 11, 123, 36);
		contentPane.add(btnPlayLocal);
		
		JButton btnHostGame = new JButton("Host Game");
		btnHostGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name = txtName.getText();
				port = Integer.parseInt(txtPort.getText());
				new HostWindow(name , port);
				dispose();
			}
		});
		btnHostGame.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnHostGame.setBounds(85, 134, 123, 36);
		contentPane.add(btnHostGame);
		
		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				name = txtName.getText();
				address = txtAdd.getText();
				try{
				port = Integer.parseInt(txtPortJ.getText());
				}catch(Exception exception){
					port = 0;
				}
				dispose();
				JoinLobby lobby = new JoinLobby(name,address, port);
			}
		});
		btnJoinGame.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnJoinGame.setBounds(89, 224, 126, 36);
		contentPane.add(btnJoinGame);
		
		txtPort = new JTextField();
		txtPort.setBounds(121, 179, 74, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Port: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(85, 181, 46, 14);
		contentPane.add(lblNewLabel);
		
		txtAdd = new JTextField();
		txtAdd.setBounds(21, 288, 116, 20);
		contentPane.add(txtAdd);
		txtAdd.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAddress.setBounds(55, 271, 69, 14);
		contentPane.add(lblAddress);
		
		txtPortJ = new JTextField();
		txtPortJ.setBounds(173, 288, 86, 20);
		contentPane.add(txtPortJ);
		txtPortJ.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPort.setBounds(197, 271, 32, 14);
		contentPane.add(lblPort);
		
		lblError.setForeground(Color.RED);
		lblError.setBounds(84, 319, 126, 14);
		lblError.setVisible(false);
		contentPane.add(lblError);
	}
	
	public static void displayError(){
		lblError.setVisible(true);
	}
}
