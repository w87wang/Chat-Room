import javax.swing.JFrame;


public class RunServer {

	public static void main (String [] args) {
		Server chatroom = new Server();
		chatroom.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		chatroom.startRunning();
	}
}
