package client_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import view.JassGameView;


/* Concept for our Client/Server was this resource
 * https://www.dreamincode.net/forums/topic/259777-a-simple-chat-program-with-clientserver-gui-optional/
 * 
 * We changed the methods to our own JassGameView
 
 * Author: Visnuciry
 */

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  {

	// for I/O
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;
	private JassGameView view;

	// the server, the port and the username
	private String server, username;
	private int port;

	// We added the JassGameView view to the constructor
	public Client(String server, int port, String username,  JassGameView view) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.view = view;
	}
	
	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
		// if it failed not much I can do so
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);
	
		/* Creating both Data Stream */
		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server 
		new ListenFromServer().start();
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			return false;
		}
		// success we inform the caller that it worked
		return true;
	}

	/*
	 * To send a message to the console
	 */
	private void display(String msg) {
		
			System.out.println(msg);      // println in console mode
	}
	
	/*
	 * To send a message to the server
	 */
	public void sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	
	
	public static void main(String[] args) {
		// default values
		int portNumber = 1500;
		String serverAddress = "localhost";
		String userName = "Anonymous";

		// We (SEVO) left this in the code to be sure that if something goes wrong, nothing bad happens
		// depending of the number of arguments provided we fall through
		switch(args.length) {
			// > javac Client username portNumber serverAddr
			case 3:
				serverAddress = args[2];
			// > javac Client username portNumber
			case 2:
				try {
					portNumber = Integer.parseInt(args[1]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
					return;
				}
			// > javac Client username
			case 1: 
				userName = args[0];
			// > java Client
			case 0:
				break;
			// invalid number of arguments
			default:
				System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
			return;
		}
		
		// create the Client object
		Client client = new Client(serverAddress, portNumber, userName, null);
		// test if we can start the connection to the Server
		// if it failed nothing we can do
		if(!client.start())
			return;
		
			
	}

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a GUI or simply System.out.println() it in console mode
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) sInput.readObject();
					
					/* NEW
					 * Identifies the ChatMessaging
					 */
					if(view != null) {
						String[] tempArray = msg.split(":");
						//									  Message Type     player count
						view.updateFromServer(Integer.parseInt(tempArray[0]), tempArray[1]) ;
					}
				}
				catch(IOException e) {
					display("Server has close the connection: " + e);
					
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}
