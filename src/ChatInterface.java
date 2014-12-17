import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Choice;
import javax.swing.JComboBox;

import server.ChatMessage;


public class ChatInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JTextField paramTextField;
	private JTextArea incomingTextArea;
	private JComboBox typeComboBox;
	private boolean listener;

	String username;
	Socket socket;	
	ObjectInputStream breader;
	ObjectOutputStream writer;
	ArrayList<String> userList = new ArrayList<String>();
	Boolean isConnected = false;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatInterface frame = new ChatInterface();
					//ChatInterface2 frame1 = new ChatInterface2();
					frame.setVisible(true);
					//frame1.setVisible(true);
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
	
	public void Initialise() {
		setBackground(new Color(238, 238, 238));
		setForeground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 421);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JLabel lblSignedInAs = new JLabel("Signed in as");
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
				if (isConnected == false) {
					username = usernameTextField.getText();	
										
					lblSignedInAs.setText("Signed in as " +username);
					
					try {
						socket = new Socket("localhost", 1500); //IP Address and port number "localhost", 1500
						breader = new ObjectInputStream(socket.getInputStream());
						writer = new ObjectOutputStream(socket.getOutputStream());
						writer.writeObject(new ChatMessage(3, username, null));
						System.out.println(username + "Has connected ");
						writer.flush();
						isConnected = true;
					}
					catch (Exception e) {
						System.out.println("Cannot connect try again.\n");
					}
					listener = true;
	            	ListenThread();
					}
					else if (isConnected == true) {
						System.out.println("You are already connected");
					}
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
		
		JLabel lblParameter = new JLabel("Parameter:");
		lblParameter.setFont(new Font("Avenir", Font.PLAIN, 13));
		lblParameter.setForeground(Color.GRAY);
		lblParameter.setBounds(22, 110, 98, 16);
		contentPane.add(lblParameter);

		paramTextField = new JTextField();
		paramTextField.setBounds(95, 109, 134, 21);
		contentPane.add(paramTextField);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(26, 158, 1, 16);
		contentPane.add(textArea);
		
		final JTextArea outgoingTextArea = new JTextArea();
		outgoingTextArea.setBounds(21, 156, 282, 162);
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);		
		outgoingTextArea.setBorder(border);
		getContentPane().add(outgoingTextArea);
		contentPane.add(outgoingTextArea);
		
		JButton btnSendOutgoing = new JButton("Send");
		btnSendOutgoing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("test");
								
								// TODO add your handling code here:
						        String nothing = "";
						        if ((outgoingTextArea.getText()).equals(nothing)) {
						        	outgoingTextArea.setText("");
						        	outgoingTextArea.requestFocus();
						        } 
						        
						        System.out.println("combo box text: " +typeComboBox.getSelectedItem().toString());
						        
						        
						        	try {
						        		
						        		int type = 1;
						            	if(typeComboBox.getSelectedItem().toString().equalsIgnoreCase("LIST")){
						            		type = 4;
						            	}
						            	if(typeComboBox.getSelectedItem().toString().equalsIgnoreCase("MESSAGE")){
						            		System.out.println("ENTER");
						            		type = 1;
						            		
						            	}
						            	if(typeComboBox.getSelectedItem().toString().equalsIgnoreCase("HAIL")){
						            		type = 5;
						            	}
						            	if(typeComboBox.getSelectedItem().toString().equalsIgnoreCase("QUIT")){
						            		type = 2;
						            	
						            	}
						            	if(typeComboBox.getSelectedItem().toString().equalsIgnoreCase("STAT")){
						            		type = 0;
						            	}

						            	writer.writeObject(new ChatMessage(type, paramTextField.getText().toString(),outgoingTextArea.getText().toString()));
						            	if(type == 2){
						            		Disconnect();
						            		listener = false;
						            	}
						        	} catch (Exception ex) {
						                System.out.println("Message was not sent. \n");
						            }
						        	outgoingTextArea.setText("");
						        	outgoingTextArea.requestFocus();	
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
		getContentPane().add(incomingTextArea);
		contentPane.add(incomingTextArea);
		
		JLabel lblOutgoing = new JLabel("Outgoing Messages:");
		lblOutgoing.setForeground(Color.GRAY);
		lblOutgoing.setFont(new Font("Avenir", Font.PLAIN, 13));
		lblOutgoing.setBounds(21, 139, 144, 16);
		contentPane.add(lblOutgoing);
		
		typeComboBox = new JComboBox();
		typeComboBox.setBounds(85, 78, 134, 20);
		typeComboBox.addItem("Message");
		typeComboBox.addItem("Quit");
		typeComboBox.addItem("Stat");
		typeComboBox.addItem("Hail");
		typeComboBox.addItem("list");
		contentPane.add(typeComboBox);

	}
		
	public class IncomingReader implements Runnable {

			@Override
		public void run() {
				
				while(listener){
					try{
						String msg = (String) breader.readObject();
						System.out.println("Message " +msg);
						incomingTextArea.append(msg + "\n");
					}
					catch(IOException | ClassNotFoundException e){
						System.out.println("test");
						System.out.println(e);
					}
				}
				
		}
			
			}//Incoming
	
	public void ListenThread(){
		Thread IncomingReader = new Thread(new IncomingReader());
		IncomingReader.start();
	}
	

    public void Disconnect() {
        try {
               System.out.println("Disconnected.\n");
               socket.close();
        } catch(Exception ex) {
               System.out.println("Failed to disconnect. \n");
        }
        isConnected = false;
      }
	}
