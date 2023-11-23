package org.NetworkInterface;

import java.util.Queue;
import java.util.LinkedList;
import de.upb.swtpra1819interface.parser.*;
import de.upb.swtpra1819interface.messages.*;

public class NetworkServer {
	private Queue<MessageWithClientId> msgQueue;
	private Parser parser;
	private MultiServer server = null;
	private Thread thread = null;

	/**
	 * @param port Port that should be opened
	 */
	public NetworkServer(short port) {
		super();
		this.msgQueue = new LinkedList<MessageWithClientId>();
		this.parser = new Parser();
		server = new MultiServer(port, this);
		thread = new Thread(server);
		thread.start();
	}

	/**
	 * @param msg      Message that was received
	 * @param clientId Id of Sender
	 * @throws ParsingException
	 */
	public synchronized void addToMsgQueue(String msg, long clientId) throws ParsingException {
		if (msg != null && !"".equals(msg)) {
			MessageWithClientId msg1 = new MessageWithClientId(parser.deserialize(msg), clientId);
			msgQueue.add(msg1);
		}
	}

	/**
	 * @param msg Message that should be transmitted to all clients
	 */
	public void sendMsg(Message msg) {
		server.sendMsg(parser.serialize(msg));
	}

	/**
	 * @param msg      Message that should be transmitted
	 * @param clientId Receiver ID
	 */
	public void sendMsg(Message msg, long clientId) {
		server.sendMsg(parser.serialize(msg), clientId);
	}

	/**
	 * @return The next Message with its client id is returned from the message
	 *         queue
	 */
	public MessageWithClientId getNextMsg() {
		return msgQueue.poll();
	}

	/**
	 * @param clientId ID of the client that should be kicked
	 * @return boolean value whether kick was successful
	 */
	public boolean kick(long clientId) {
		return server.kick(clientId);
	}

	/**
	 * shuts the server down
	 */
	public void shutdown() {
		if (thread != null) {
			server.shutdown();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
