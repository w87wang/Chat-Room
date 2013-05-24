import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{

   private JTextField userText;
   private JTextArea chatWindow;
   private ObjectOutputStream out;
   private ObjectInputStream in;
   private ServerSocket server;
   private Socket connection;
   
   //Constructor
   public Server()
   {
      super("Chat Room - SERVER");
      userText = new JTextField();
      userText.setEditable(false);
      
      userText.addActionListener(
            new ActionListener()
            {
               public void actionPerformed(ActionEvent event)
               {
                  sendMessage(event.getActionCommand());
                  userText.setText("");
               }
            });
      add(userText, BorderLayout.SOUTH);
      chatWindow = new JTextArea();
      add(new JScrollPane(chatWindow));
      setSize(300, 150);
      setVisible(true);
   }
   
   //set up and run the server
   public void startRunning()
   {
      try
      {
         server = new ServerSocket(6789, 100);
         while(true)
         {
            try
            {
               waitForConnection();
               setupStreams();
               whileChatting();
            }catch(EOFException eofException)
            {
               showMessage("\n Server ended the connection!");
            }finally{
               close();
            }
         }
      }catch(IOException ioException){
         ioException.printStackTrace();
      }
   }
   
   //displays messages
   private void showMessage(final String message)
   {
      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run()
            {
               chatWindow.append(message + "\n");
            }
         }
      );
   }
   
   //sends the messages back and forth
   private void sendMessage(String input)
   {
      try
      {
         out.writeObject("SERVER - " + input);
         out.flush();
         showMessage("SERVER - " + input);
      }catch(IOException ioException)
      {
         chatWindow.append("ERROR: Can't send message");
      }
      
   }
   
   //waits for someone to connect
   private void waitForConnection() throws IOException
   {
      showMessage("Waiting for connection...");
      connection = server.accept();
      showMessage("Now connected to " + connection.getInetAddress().getHostName());
   }
   
   //Sets up the connections
   private void setupStreams() throws IOException
   {
      out = new ObjectOutputStream(connection.getOutputStream());
      out.flush();
      in = new ObjectInputStream(connection.getInputStream());
      showMessage("\nStreams are now setup!");
      
      
   }
   
   //during the chat
   private void whileChatting() throws IOException
   {
      String message = "You are now connected!";
      showMessage(message);
      ableToType(true);
      do{
         //have conversation
         try
         {
            message = (String) in.readObject();
            showMessage(message);
         }catch(ClassNotFoundException classNotFoundException)
         {
            showMessage("\n Invalid Input");
         }
      }while(!message.equals("CLIENT - END"));
   }
   
   //cleans everything up
   private void close()
   {
      showMessage("Closing Connection...");
      ableToType(false);
      try
      {
         out.close();
         in.close();
         connection.close();
      }catch(IOException ioException)
      {
         ioException.printStackTrace();
      }
      
   }
   
   private void ableToType(final boolean able)
   {
      SwingUtilities.invokeLater(
            new Runnable()
            {
               public void run()
               {
                  userText.setEditable(able);
               }
            }
         );
   }
}