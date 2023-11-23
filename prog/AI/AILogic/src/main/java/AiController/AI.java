package AiController;

import org.NetworkInterface.NetClient;

/**
 * The AI is initialised here.
 */
public class AI {

	/**
	 * Initialise the AI with the IP and port of the server and a name that is
	 * displayed in the game
	 * 
	 * @param ip         IP of the server
	 * @param port       Port of the server
	 * @param clientName Name that is displayed in the game
	 */
	public AI(String ip, int port, String clientName) {
		NetClient client = new NetClient(port, ip);
		System.out.println("Try to connect to: " + ip + ":" + port + "...");
		new AiController(client, clientName);
	}

}
