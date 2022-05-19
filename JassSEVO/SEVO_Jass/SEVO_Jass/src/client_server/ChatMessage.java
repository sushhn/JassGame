package client_server;

import java.io.*;

/* Concept for our Client/Server was this resource
 * https://www.dreamincode.net/forums/topic/259777-a-simple-chat-program-with-clientserver-gui-optional/
 * 
 * We changed the methods to our own JassGameView
 * 
 * Author: Visnuciry
 */

/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	/* The different types of message sent by the Client
	 * LOGOUT to disconnect from the Server	 * 
	 * CONNECTED to connect the Clients/players
	 * READY_TO_PLAY waits for 4 connected players, sets the random trumpf and
	 * sets the player names. Button "Start Game" will be enabled for the first player.
	 * Connected players in the top right of the view will be updated to 4 after 4 are connected.
	 * DISTRIBUTE_CARDS to distribute the card to each player (server side)
	 * SEND_CARDS to send the cards to each player and to disable the "Start Game" button
	 * DISPLAY_CARDS to send the player the information which cards he can play
	 */
	 
	public static final int LOGOUT = 1, CONNECTED = 2, READY_TO_PLAY = 3, DISTRIBUTE_CARDS = 4, SEND_CARDS = 5, DISPLAY_CARD = 6;
	
	private int type;
	private String message;
	
	// constructor
	public ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	// getters
	int getType() {
		return type;
	}
	
	String getMessage() {
		return message;
	}
}

