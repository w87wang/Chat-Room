import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Client extends JFrame  {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	//constructor 
	public Client (String host) {
		super("Chat Room - Client!");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
		);
		add (userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	public void startRunning () {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			close();
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException {
		showMessages ("Attempting Connection.. \n");
		connection = new Socket (InetAddress.getByName(serverIP), 6789);
		showMessages("Connect to: " + connection.getInetAddress().getHostName());
		
		
	}
	
	
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream (connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream (connection.getInputStream());
		showMessages ("\n Streams are connected \n");
	}
	
	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessages ("\n " + message);
				
			} catch (ClassNotFoundException classNotFoundException){
				showMessages (" \n Invalid Object Type \n");
				
			}
		} while (!message.equals("SERVER - END"));
	}

	private void close() {
		showMessages ("\n Shuting Down... \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			
			ioException.printStackTrace();
		}
	}
	
	//send message to server
	private void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessages("\n CLIENT - "+ message);
		} catch (IOException ioException) {
			chatWindow.append("\n Something went wrong \n");
		}
	}
	
	private void showMessages (final String m) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(m);
					}
				});
	}
	
	private void ableToType (final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				});
	}
	
}
