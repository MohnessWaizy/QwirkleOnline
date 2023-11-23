package org.NetworkInterface;

import java.util.concurrent.ConcurrentLinkedQueue;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

/**
 * 
 * Client side of network communication
 *
 */
public class Client {
	private ConcurrentLinkedQueue<Message> msgQueue;
	private Parser parser;
	private NetworkClient client = null;
	private Thread thread = null;

	/**
	 * @param port
	 *            port to connect to
	 * @param ip
	 *            ip to connect to
	 */
	public Client(short port, String ip) {
		super();
		this.msgQueue = new ConcurrentLinkedQueue<Message>();
		this.parser = new Parser();
		client = new NetworkClient(port, this, ip);
		thread = new Thread(client);
		thread.start();
	}

	/**
	 * Adds a new message to the message queue of the client
	 * 
	 * @param msg
	 *            Message that is received
	 * @throws ParsingException
	 */
	public synchronized void addToMsgQueue(String msg) throws ParsingException {
		try {
			if (msg.length() > 0) {
				msgQueue.add(parser.deserialize(msg));
			}
		} catch (IllegalStateException e) {
			// not a json so ignore it. The server can always be trusted.
		}
	}

	/**
	 * Sends a message to the server
	 * 
	 * @param msg
	 *            Message that should be transmitted
	 */
	public void sendMsg(Message msg) {
		client.sendMsg(parser.serialize(msg));
	}

	/**
	 * Gets the next message from the message queue
	 * 
	 * @return gets the first Message object in the MessageQueue
	 */
	public Message getNextMsg() {
		return msgQueue.poll();
	}

	/**
	 * called to disconnect from Server
	 */
	public void disconnect() {
		if (thread != null) {
			client.disconnect();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
