package server;


import java.io.*;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	// The different types of message sent by the Client:
	// LIST to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	public static final int STAT = 0, MESSAGE = 1, QUIT = 2, IDEN = 3, LIST = 4, HAIL = 5;
	private int type;
	private String param;
	private String message;
	
	// constructor
	ChatMessage(int type, String param, String message) {
		this.type = type;
		this.param = param;
		this.message = message;
	}
	
	// getters
	public int getType() {
		return type;
	}
	
	public String getParam(){
		return param;
	}
	
	public String getMessage() {
		return message;
	}
}


