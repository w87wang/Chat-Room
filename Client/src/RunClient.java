
import javax.swing.JFrame;

public class RunClient {
	public static void main(String [] args) {
		Client newChat = new Client("127.0.0.1");
		newChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newChat.startRunning();
	}
}
