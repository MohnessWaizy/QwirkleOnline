package parser;

import org.NetworkInterface.NetClient;

import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.NotAllowed;

/**
 * Interface for the parser.
 */
public abstract class InterfaceParser implements ParserClockInterface {

	protected NetClient client;
	private ParserClock clock;

	public NetClient getClient() {
		return client;
	}

	public void setClient(NetClient client) {
		this.client = client;
	}

	public void initClock(long cycle) {
		clock = new ParserClock(cycle, this);
	}

	public void startQueueWorker() {
		clock.startTicking();
	}

	/**
	 * Parser will be stopped. Clock thread will be destroyed. You should call this
	 * function when parse is not needed anymore.
	 */
	public void stopParser() {
		clock.destroy();
	}

	/**
	 * Parser will be paused. No messages will be parsed.
	 */
	public void pauseParser() {
		clock.pauseTicking();
	}

	/**
	 * Parser will be resumed. Messages in queue will be parsed again.
	 */
	public void resumeParser() {
		clock.resumeTicking();
	}

	/**
	 * Processes all incoming messages from server
	 * 
	 * @param msg Message object with information from the server
	 */
	public void parseMessage(Message msg) {

	}

	/**
	 * get all messages from the server
	 */
	public final void getAllQueueMessages() {
		Message msg;
		if (client != null) {
			while ((msg = client.getNextMsg()) != null) {
				parseMessage(msg);
			}
		}
	}

}
