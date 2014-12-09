import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import server.ChatMessage;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.net.*;
import java.util.*;
import java.io.*;


public class ChatInterface {

	public JFrame frame;
	private JTextField usernameTextField;
	private JTextField passwordTextField;
	private JTextField OutgoingMessages;
	private JTextArea incomingMessages;
	private JTextField textField_1;
	private JTextField textField;

	private String username;
	private Socket socket;
	ObjectInputStream breader;
	ObjectOutputStream writer;
	ArrayList<String> userList = new ArrayList<String>();
	Boolean isConnected = false;

	/**
	 * Create the application.
	 */
	public ChatInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 650, 469);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(36, 6, 73, 16);
		frame.getContentPane().add(lblNewLabel);
		
		usernameTextField = new JTextField();
		usernameTextField.setBounds(6, 23, 134, 28);
		frame.getContentPane().add(usernameTextField);
		usernameTextField.setColumns(10);
		
		incomingMessages = new JTextArea();
		incomingMessages.setBounds(175, 26, 297, 83);
		frame.getContentPane().add(incomingMessages);
		incomingMessages.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(175, 133, 297, 28);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblType_1 = new JLabel("Type:");
		lblType_1.setBounds(175, 115, 61, 16);
		frame.getContentPane().add(lblType_1);
		
		JLabel lblParameter = new JLabel("Parameter:");
		lblParameter.setBounds(175, 173, 73, 16);
		frame.getContentPane().add(lblParameter);
		
		textField = new JTextField();
		textField.setBounds(175, 189, 297, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		final JLabel lblSignedInAs = new JLabel("Signed in as");
		lblSignedInAs.setBounds(6, 143, 114, 46);
		frame.getContentPane().add(lblSignedInAs);
		
		JButton sendLoginDetails = new JButton("Send");
		sendLoginDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
								
				if (isConnected == false) {
				username = usernameTextField.getText();	
				
				usernameTextField.setEditable(false);
				passwordTextField.setEditable(false);
				
				lblSignedInAs.setText("Signed in as " +username);
				
				try {
					socket = new Socket("localhost", 1500); //IP Address and port number 
					InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
					breader = new ObjectInputStream(socket.getInputStream());
					//writer = new PrintWriter(socket.getOutputStream()); 
					writer = new ObjectOutputStream(socket.getOutputStream());
					writer.writeObject(new ChatMessage(3, username, null));
					System.out.println(username + "Has connected ");
					writer.flush();
					isConnected = true;
				}
				catch (Exception e) {
					System.out.println("Cannot connect try again.\n");
					usernameTextField.setEditable(true);
					passwordTextField.setEditable(true);					
				}
				ListenThread();
				}
				else if (isConnected == true) {
					System.out.println("You are already connected");
				}
			}
		});
		sendLoginDetails.setBounds(16, 102, 117, 29);
		frame.getContentPane().add(sendLoginDetails);
		
		passwordTextField = new JTextField();
		passwordTextField.setBounds(6, 69, 134, 28);
		frame.getContentPane().add(passwordTextField);
		passwordTextField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(36, 52, 73, 16);
		frame.getContentPane().add(lblPassword);
		
		OutgoingMessages = new JTextField();
		OutgoingMessages.setBounds(175, 257, 297, 146);
		frame.getContentPane().add(OutgoingMessages);
		OutgoingMessages.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Incoming Messages:");
		lblNewLabel_1.setBounds(184, 6, 134, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton sendMessage = new JButton("Send");
		sendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("test");
				
				// TODO add your handling code here:
		        String nothing = "";
		        if ((OutgoingMessages.getText()).equals(nothing)) {
		        	OutgoingMessages.setText("");
		        	OutgoingMessages.requestFocus();
		        } 
		        
		        	try {
		            	int type = 1;
		            	if(textField_1.getText().equalsIgnoreCase("LIST")){
		            		type = 4;
		            	}
		            	if(textField_1.getText().equalsIgnoreCase("MESSAGE")){
		            		type = 1;
		            	}
		            	if(textField_1.getText().equalsIgnoreCase("HAIL")){
		            		type = 5;
		            	}
		            	if(textField_1.getText().equalsIgnoreCase("QUIT")){
		            		type = 2;
		            	}
		            	if(textField_1.getText().equalsIgnoreCase("STAT")){
		            		type = 0;
		            	}
		            	System.out.println("sending message");
		            	writer.writeObject(new ChatMessage(type, 
		            			textField.getText().toString(), 
		            			OutgoingMessages.getText().toString()));
		            	
		            } catch (Exception ex) {
		                System.out.println("Message was not sent. \n");
		            }
		            OutgoingMessages.setText("");
		            OutgoingMessages.requestFocus();
		        

		        OutgoingMessages.setText("");
		        OutgoingMessages.requestFocus();
				
			}
		});
		sendMessage.setBounds(484, 354, 87, 69);
		frame.getContentPane().add(sendMessage);
		
		JLabel lblOnline = new JLabel("Online:");
		lblOnline.setBounds(510, 6, 61, 16);
		frame.getContentPane().add(lblOnline);
		
		JLabel OnlineContacts = new JLabel("(list of online users)");
		OnlineContacts.setForeground(Color.BLACK);
		OnlineContacts.setBackground(Color.PINK);
		OnlineContacts.setBounds(498, 23, 134, 330);
		frame.getContentPane().add(OnlineContacts);
		
		JLabel lblOutgoingMessages = new JLabel("Outgoing Messages:");
		lblOutgoingMessages.setBounds(175, 241, 155, 16);
		frame.getContentPane().add(lblOutgoingMessages);
	}
	
	public class IncomingReader implements Runnable {

		@Override
	public void run() {

			
			while(true){
				try{
					String msg = (String) breader.readObject();
					incomingMessages.append(msg);
				}
				catch(IOException e){
					System.out.println(e);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	}
	}
	
	public void ListenThread(){
		Thread IncomingReader = new Thread(new IncomingReader());
		IncomingReader.start();
	}

	public void userAdd(String data) {
		userList.add(data);
	}

	public void userRemove(String data) {
		userList.remove(data);
	}	

	public void writeUsers() throws IOException{
		//generates online user list
		String[] tempList = new String[(userList.size())]; 
		userList.toArray(tempList);
		for(String token:tempList) {
			((Appendable) userList).append(token + "\n");
		}
	
	
	}

	public void sendDisconnect() {
		
	        try{
	            writer.writeObject(new ChatMessage(2, null, null)); // Sends server the disconnect signal.
	            writer.flush(); // flushes the buffer
	        } catch (Exception e) {
	            System.out.println("Could not send Disconnect message.\n");
	        }

	      }

    public void Disconnect() {
        try {
               System.out.println("Disconnected.\n");
               socket.close();
        } catch(Exception ex) {
               System.out.println("Failed to disconnect. \n");
        }
        isConnected = false;
        //.setEditable(true);
       // usersList.setText("");

      }
                             
}





