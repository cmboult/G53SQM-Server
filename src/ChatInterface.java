package chat;

import java.awt.EventQueue;



public class ChatInterface {

	private JFrame frame;
	private JTextField usernameTextField;
	private JTextField passwordTextField;
	private JTextField OutgoingMessages;
	private JTextField incomingMessages;

	String username;
	String password;
	Socket socket;
	BufferedReader breader;
	PrintWriter writer;
	ArrayList<String> userList = new ArrayList<String>();
	Boolean isConnected = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatInterface window = new ChatInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		
		incomingMessages = new JTextField();
		incomingMessages.setBounds(175, 25, 297, 173);
		frame.getContentPane().add(incomingMessages);
		incomingMessages.setColumns(10);
		
		 final JLabel lblSignedInAs = new JLabel("Signed in as");
		lblSignedInAs.setBounds(6, 143, 114, 46);
		frame.getContentPane().add(lblSignedInAs);
		
		JButton sendLoginDetails = new JButton("Send");
		sendLoginDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
								
				if (isConnected == false) {
				username = usernameTextField.getText();	
				password = passwordTextField.getText();
				
				usernameTextField.setEditable(false);
				passwordTextField.setEditable(false);
				
				lblSignedInAs.setText("Signed in as " +username);
				
				try {
					socket = new Socket("192.168.0.11", 21); //IP Address and port number 
					InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
					breader = new BufferedReader(streamReader);
					writer = new PrintWriter(socket.getOutputStream()); 
					//writer.println(username + "Has connected ");
					System.out.print(username + "Has connected ");
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
		OutgoingMessages.setBounds(175, 223, 297, 200);
		frame.getContentPane().add(OutgoingMessages);
		OutgoingMessages.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Incoming Messages:");
		lblNewLabel_1.setBounds(184, 6, 134, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton sendMessage = new JButton("Send");
		sendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		lblOutgoingMessages.setBounds(185, 201, 155, 16);
		frame.getContentPane().add(lblOutgoingMessages);
	}
	
	public class IncomingReader implements Runnable {

		@Override
	public void run() {
			String stream;
			String[] data;
			String done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";
			
			try {
				while ((stream = breader.readLine()) != null) {
				data = stream.split("Y");
				
				if(data[2].equals(chat)){
					System.out.println(data[0] + ":" + data[1] + "\n");			
				}
				else if (data[2].equals(connect)){
					userAdd(data[0]);
				}
				else if (data[2].equals(disconnect)){
					userRemove(data[0]);
				} 
				else if (data[2].equals(done)) {
					//clear user list
				}
			}
		}
		catch (Exception e) {
				
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

	       String bye = (username + ": :Disconnect");
	        try{
	            writer.println(bye); // Sends server the disconnect signal.
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

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        String nothing = "";
        if ((OutgoingMessages.getText()).equals(nothing)) {
        	OutgoingMessages.setText("");
        	OutgoingMessages.requestFocus();
        } else {
            try {
               writer.println(username + ":" + OutgoingMessages.getText() + ":" + "Chat");
               writer.flush(); // flushes the buffer
            } catch (Exception ex) {
                System.out.println("Message was not sent. \n");
            }
            OutgoingMessages.setText("");
            OutgoingMessages.requestFocus();
        }

        OutgoingMessages.setText("");
        OutgoingMessages.requestFocus();
    }                                  
}





