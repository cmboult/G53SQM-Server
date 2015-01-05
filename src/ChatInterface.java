import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import server.ChatMessage;

public class ChatInterface extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JTextField paramTextField;
	private JTextArea incomingTextArea;
	private JComboBox<String> typeComboBox;
	private boolean listener;
	
	JLabel lblSignedInAs;
	JTextArea outgoingTextArea;
	JLabel lblParameter;
	String username;
	Socket socket;
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	ArrayList<String> userList = new ArrayList<String>();
	Boolean isConnected = false;
	
	int type = 1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatInterface frame = new ChatInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public ChatInterface() {
		Initialise();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setUI() {
		setBackground(new Color(238, 238, 238));
		setForeground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 421);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblSignedInAs = new JLabel("Signed in as");
		lblSignedInAs.setForeground(Color.WHITE);
		lblSignedInAs.setFont(new Font("Avenir", Font.PLAIN, 12));
		lblSignedInAs.setBounds(392, 42, 159, 16);
		contentPane.add(lblSignedInAs);
		
		JLabel lblusernameTitle = new JLabel("Username:");
		lblusernameTitle.setForeground(Color.WHITE);
		lblusernameTitle.setFont(new Font("Avenir", Font.PLAIN, 13));
		lblusernameTitle.setBounds(392, 8, 98, 34);
		contentPane.add(lblusernameTitle);
	
		usernameTextField = new JTextField();
		usernameTextField.setBounds(459, 10, 103, 28);
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);
		JButton btnSendUsername = new JButton("Send");
		
		btnSendUsername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openSocket();
				outgoingTextArea.setEditable(true);
				incomingTextArea.setEditable(true);
			}
		});
		btnSendUsername.setForeground(Color.GRAY);
		btnSendUsername.setFont(new Font("Avenir", Font.PLAIN, 13));
		btnSendUsername.setBounds(568, 11, 66, 28);
		contentPane.add(btnSendUsername);
		
		JLabel lblTitleOfProject = new JLabel("Chat Room");
		lblTitleOfProject.setBackground(new Color(135, 206, 250));
		lblTitleOfProject.setOpaque(true);
		lblTitleOfProject.setFont(new Font("Avenir", Font.PLAIN, 34));
		lblTitleOfProject.setForeground(Color.WHITE);
		lblTitleOfProject.setBounds(0, 6, 634, 52);
		contentPane.add(lblTitleOfProject);
		
		lblParameter = new JLabel("Parameter:");
		lblParameter.setFont(new Font("Avenir", Font.PLAIN, 13));
		lblParameter.setForeground(Color.GRAY);
		lblParameter.setBounds(22, 110, 98, 16);
		
		paramTextField = new JTextField();
		paramTextField.setBounds(85, 109, 134, 21);
		outgoingTextArea = new JTextArea();
		outgoingTextArea.setBounds(21, 156, 282, 162);
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		outgoingTextArea.setBorder(border);
		JScrollPane outgoingScroll = new JScrollPane (outgoingTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);         
		outgoingScroll.setBounds(21, 156, 282, 162);
		outgoingScroll.setBorder(border);
		getContentPane().add(outgoingScroll);
		contentPane.add(outgoingScroll);
		
		JButton btnSendOutgoing = new JButton("Send");
		btnSendOutgoing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage(type);
			}
		});
		btnSendOutgoing.setFont(new Font("Avenir", Font.PLAIN, 13));
		btnSendOutgoing.setForeground(Color.GRAY);
		btnSendOutgoing.setBounds(186, 324, 117, 38);
		contentPane.add(btnSendOutgoing);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setFont(new Font("Avenir", Font.PLAIN, 13));
		lblType.setForeground(Color.GRAY);
		lblType.setBounds(26, 77, 61, 21);
		contentPane.add(lblType);
		
		JLabel lblIncoming = new JLabel("Incoming Messages:");
		lblIncoming.setForeground(Color.GRAY);
		lblIncoming.setFont(new Font("Avenir", Font.PLAIN, 13));
		lblIncoming.setBounds(328, 139, 134, 16);
		contentPane.add(lblIncoming);
		
		incomingTextArea = new JTextArea();
		incomingTextArea.setBounds(328, 156, 275, 162);
		border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		incomingTextArea.setBorder(border);
		JScrollPane incomingScroll = new JScrollPane (incomingTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);         
		incomingScroll.setBounds(328, 156, 275, 162);
		incomingScroll.setBorder(border);
		getContentPane().add(incomingScroll);
		contentPane.add(incomingScroll);
		
		JLabel lblOutgoing = new JLabel("Outgoing Messages:");
		lblOutgoing.setForeground(Color.GRAY);
		lblOutgoing.setFont(new Font("Avenir", Font.PLAIN, 13));
		lblOutgoing.setBounds(21, 139, 144, 16);
		contentPane.add(lblOutgoing);
		
		typeComboBox = new JComboBox();
		typeComboBox.setBounds(85, 78, 134, 20);
		typeComboBox.addItem("Options");
		typeComboBox.addItem("Message");
		typeComboBox.addItem("Quit");
		typeComboBox.addItem("Stat");
		typeComboBox.addItem("Hail");
		typeComboBox.addItem("list");
		contentPane.add(typeComboBox);
		
		typeComboBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {				
				if (typeComboBox.getSelectedItem().toString()
						.equalsIgnoreCase("OPTIONS")) {
					type = 6;
				}
				else if (typeComboBox.getSelectedItem().toString()
						.equalsIgnoreCase("LIST")) {
					type = 4;
				}
				else if (typeComboBox.getSelectedItem().toString()
						.equalsIgnoreCase("MESSAGE")) {
					type = 1;
					contentPane.add(paramTextField);
					contentPane.add(lblParameter);
					paramTextField.requestFocus();
				}
				else if (typeComboBox.getSelectedItem().toString()
						.equalsIgnoreCase("HAIL")) {
					type = 5;
				}
				else if (typeComboBox.getSelectedItem().toString()
						.equalsIgnoreCase("QUIT")) {
					type = 2;
					lblSignedInAs.setText("");
					incomingTextArea.setText("");
					}
				else if (typeComboBox.getSelectedItem().toString()
						.equalsIgnoreCase("STAT")) {
					type = 0;
				}
				
				if(!typeComboBox.getSelectedItem().toString()
						.equalsIgnoreCase("MESSAGE")) {
					contentPane.remove(paramTextField);
					contentPane.remove(lblParameter);
					sendMessage(type);
				}
				contentPane.repaint();
			}});
	}
	
	private void openSocket() {
		if (isConnected == false) {
			username = usernameTextField.getText();
			lblSignedInAs.setText("Signed in as " + username);
			try {
				socket = new Socket("localhost", 1500);
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectOutputStream.writeObject(new ChatMessage(3, username, null));
				System.out.println(username + " has connected.");
				objectOutputStream.flush();
				isConnected = true;
			} catch (Exception e) {
				System.out.println("Cannot connect try again.\n");
			}
			listener = true;
			ListenThread();
		} else if (isConnected == true) {
			System.out.println("You are already connected");
		}
	}
	
	private void sendMessage(int type) {
		String nothing = "";
		if ((outgoingTextArea.getText()).equals(nothing)) {
			outgoingTextArea.setText("");
			outgoingTextArea.requestFocus();
		}
		
		try {
			objectOutputStream.writeObject(new ChatMessage(type, paramTextField.getText()
					.toString(), outgoingTextArea.getText().toString()));
			if (type == 2) {
				Disconnect();
				listener = false;
			}
		} catch (Exception ex) {
			System.out.println("Message was not sent. \n");
		}
		outgoingTextArea.setText("");
		outgoingTextArea.requestFocus();
	}
	
	public void Initialise() {
		setUI();
	}
	
	public class IncomingReader implements Runnable {
		public void run() {
			while (listener) {
				try {
					String msg = (String) objectInputStream.readObject();
					System.out.println("Message " + msg);
					incomingTextArea.append(msg + "\n");
					String exists = "Error! User " + username + " already exists. Please try another Username.";
					if(exists.equals(msg)){
						
						isConnected = false;
						lblSignedInAs.setText("Signed in as ");

					}
				} catch (IOException | ClassNotFoundException e) {
					System.out.println(e);
				}
			}
		}
	}
	
	public void ListenThread() {
		Thread IncomingReader = new Thread(new IncomingReader());
		IncomingReader.start();
	}
	
	public void Disconnect() {
		try {
			System.out.println("Disconnected.\n");
			socket.close();
			outgoingTextArea.setEditable(false);
			incomingTextArea.setEditable(false);
		} catch (Exception ex) {
			System.out.println("Failed to disconnect. \n");
		}
		isConnected = false;
	}
}