package client_server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.JOptionPane;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	// a unique ID for each connection
	private static int uniqueId;
	// an ArrayList to keep the list of the Client
	private ArrayList<ClientThread> al;
	// to display time
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
	
	
	
	public Server(int port) {
		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		al = new ArrayList<ClientThread>();
	}
	ServerSocket serverSocket;
	public void start() {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try {
			// the socket used by the server
			serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while (keepGoing) {
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");

					Socket socket = serverSocket.accept(); // accept connection
					// if I was asked to stop
					if (!keepGoing)
						break;
					ClientThread t = new ClientThread(socket); // make a thread of it
								
					al.add(t); // save it in the ArrayList
					t.start();
					// new
					t.writeMsg(ChatMessage.CONNECTED, Integer.toString(al.size()));
					if (al.size() == 4) {

						String msg = getReadyMessage();

						for (ClientThread clientThread : al) {
							clientThread.writeMsg(ChatMessage.READY_TO_PLAY, msg);
						}
					}
			}

			// I was asked to stop
			try {
				serverSocket.close();
				for (int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					} catch (IOException ioE) {
						// not much I can do
					}
				}
			} catch (Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
			String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}		
   
	
	// Display an event (not a message) to the console or the GUI	 
	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		
			System.out.println(time);
	}
	
	
	private String getReadyMessage() {
		
		StringBuilder sbMessage = new StringBuilder();
		
		String[] randomTrumpf = { "Hearts", "Spades", "Clubs", "Diamonds" };
		Random random = new Random();
		String randomTrumpfChosen = randomTrumpf[random.nextInt(randomTrumpf.length)];
		 
		sbMessage.append(randomTrumpfChosen);
		sbMessage.append(",");
		
		
		for (int i = 0; i < al.size(); i++) {
			
			sbMessage.append(i+1);
			sbMessage.append("-");
			sbMessage.append(al.get(i).username);
			sbMessage.append(",");
		}
		
		return sbMessage.toString();
	}
	
	
	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			// found it
			if(ct.id == id) {
				al.remove(i);
				
				return;
			}
		}
	}
	
	
	public static void main(String[] args) {
		// start server on port 1500 unless a PortNumber is specified 
		int portNumber = 1500;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		// create a server object and start it
		Server server = new Server(portNumber);
		server.start();
	}

	/** One instance of this thread will run for each client */
	public class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the Username of the Client
		String username;
		// the only type of message a will receive
		ChatMessage cm;
		// the date I connect
		String date;

		// Constructore
		ClientThread(Socket socket) {
			// a unique id
			id = ++uniqueId;
			this.socket = socket;
			/* Creating both Data Stream */
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username
				username = (String) sInput.readObject();
				display(username + " just connected.");
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			// have to catch ClassNotFoundException
			// but I read a String, I am sure it will work
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}

		// what will run forever
		public void run() {
			// to loop until LOGOUT
			boolean keepGoing = true;
			while(keepGoing) {
				// read a String (which is an object)
				try {
					cm = (ChatMessage) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				// the message part of the ChatMessage
				String message = cm.getMessage();

				// Switch on the type of message receive
				switch(cm.getType()) {

					
				case ChatMessage.LOGOUT:
                    display(username + " disconnected with a LOGOUT message.");
                    keepGoing = false;
                    
                    if(al.size()==4) {
                        
                        try {
                            
                            for (int i = 0; i < al.size(); ++i) {
                                ClientThread tc = al.get(i);
                            
                                    tc.sInput.close();
                                    tc.sOutput.close();
                                    tc.socket.close();
                            
                                    
                            
                            }
                            serverSocket.close();

 

                            
                            JOptionPane.showMessageDialog(null, "Oops.. someone disconnected from the Game\n"
                                    + "Start up the Server again", "Disconnect!", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            
                        } catch (Exception e) {
                            display("Exception closing the server and clients: " + e);
                        }
                    }
                    
                    break;
					
				case ChatMessage.DISPLAY_CARD:
					for(int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
						ct.writeMsg(ChatMessage.DISPLAY_CARD, message) ;
					}
					break;
					
				case ChatMessage.DISTRIBUTE_CARDS:
	                   
                    String [] cardArray = message.split("=");
                   
                    for (int i=0; i < cardArray.length; i++) {
                   
                        ClientThread ct = al.get(i);
                        ct.writeMsg(ChatMessage.SEND_CARDS, cardArray[i]);
                    }
                    break;
				
				}
			}
			// remove players from the arrayList containing the list of the
			// connected Clients
			remove(id);
			close();
		}
		
		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		
		/*
		 * Write a String to the Client output stream
		 */
		
		// new
		public boolean writeMsg(int msgType, String msg) {
            // if Client is still connected send the message to it
            if(!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeObject(Integer.toString(msgType) + ":" + msg);
            }
            // if an error occurs, do not abort just inform the user
            catch(IOException e) {
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }

	}
}