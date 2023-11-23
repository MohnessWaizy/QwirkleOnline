package parser;

import java.util.ArrayList;

import org.NetworkInterface.NetClient;

import de.upb.swtpra1819interface.messages.Message;

/**
 * parser for the interface
 *
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

	/**
	 * start to read all messages
	 */
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
		clock.stopTicking();
	}

	/**
	 * Parser will be resumed. Messages in queue will be parsed again.
	 */
	public void resumeParser() {
		clock.resumeTicking();
	}

	/**
	 * 
	 * @param msg
	 */
	public void parseMessage(Message msg) {
	}

	/**
	 * Redirect messages in message queue to {@link parseMessage}.
	 */
	public void getAllQueueMessages() {
		Message msg;
		if (client != null) {
			while ((msg = client.getNextMsg()) != null) {
				parseMessage(msg);
			}
		}
	}

	/**
	 * Disconnects client from server via socket.
	 */
	public void disconnect() {
		client.disconnect();
	}
}
