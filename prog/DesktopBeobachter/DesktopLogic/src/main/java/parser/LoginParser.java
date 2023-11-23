package parser;

import org.NetworkInterface.NetClient;

import controller.LoginSceneController;
import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.messages.Message;

/**
 * <i>LoginParser</i> gets used to parse messages from the Server and sends it to the LoginSceneController 
 * 
 *
 */
public class LoginParser extends InterfaceParser{
	
	private NetClient client;
	private LoginSceneController contrl;
	
	/**
	 * Konstruktor of LoginParser
	 * 
	 * @param client {@link NetClient} for getting Messages
	 * @param contrl {@link LoginSceneController} where the messages should be sent to
	 * @param cycle the cycle length in which the messages gets parsed
	 */
	public LoginParser(NetClient client, LoginSceneController contrl, long cycle) {
		this.client = client;
		this.contrl = contrl;
		setClient(client);
		initClock(cycle);
		startQueueWorker();
		
	}
	
	/**
	 * Parsing the last Message called by the {@link ParserClock}
	 * @return 
	 */
	@Override
	public void parseMessage(Message msg) {
		switch (msg.getUniqueId()) {
		case 101:
			
			ConnectAccepted ca = (ConnectAccepted) msg;
			
			contrl.loginRequestIsValid(ca.getClientId());
			break;
		case 920:
						
			contrl.loginRequestNotValid();
			
			break;

		default:
			break;
		}
	}
}
