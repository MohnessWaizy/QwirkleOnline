package org.NetworkInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import de.upb.swtpra1819interface.parser.*;
import de.upb.swtpra1819interface.messages.*;
import org.CostumMessages.Disconnect;

/**
 * 
 * Base class of the network communication. Manages all network stuff.
 *
 */
public class NetworkServer {
	private ConcurrentLinkedQueue<MessageWithClientId> msgQueue;
	private Parser parser;
	private MultiServer server = null;
	private Thread thread = null;
	private Map<Integer, Integer> kickMap = null;

	/**
	 * @param port
	 *            Port that should be opened
	 */
	public NetworkServer(int port) {
		this.msgQueue = new ConcurrentLinkedQueue<MessageWithClientId>();
		this.parser = new Parser();
		this.kickMap = new HashMap<Integer, Integer>();
		server = new MultiServer(port, this);
		thread = new Thread(server);
		thread.start();
		thread.setName("MultiServer");
	}

	/**
	 * 
	 * Adds a new message to the message queue of the Server.
	 * 
	 * @param msg
	 *            Message that was received
	 * @param clientId
	 *            Id of Sender
	 * @throws ParsingException
	 */
	public synchronized void addToMsgQueue(String msg, int clientId) throws ParsingException {
		try {
			if (msg != null && !msg.equals("")) {
				MessageWithClientId msg1;

				msg1 = new MessageWithClientId(parser.deserialize(msg), clientId);

				msgQueue.offer(msg1);
			}
		} catch (ParsingException e) {
			if (kickMap.get(clientId) > 5) {
				kick(clientId);
			} else {
				kickMap.put(clientId, kickMap.remove(clientId) + 1);
			}
			throw new ParsingException();
		}
	}

	/**
	 * 
	 * Adds a special message to the message queue, so that the rest of the server
	 * knows that a client is disconnected
	 *
	 * @param clientId
	 *            Id of the disconnected client
	 */
	public synchronized void clientDisconnected(int clientId) {
		msgQueue.offer(new MessageWithClientId(new Disconnect(clientId), clientId));
		kickMap.remove(clientId);
	}

	/**
	 * 
	 * Sends a new message to all connected clients on the server
	 * 
	 * @param msg
	 *            Message that should be transmitted to all clients
	 */
	public void sendMsg(Message msg) {
		server.sendMsg(parser.serialize(msg));
	}

	/**
	 * 
	 * Sends a message to a specified client
	 * 
	 * @param msg
	 *            Message that should be transmitted
	 * @param clientId
	 *            Receiver ID
	 */
	public void sendMsg(Message msg, int clientId) {
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
	 * 
	 * Kicks a client from the server
	 * 
	 * @param clientId
	 *            ID of the client that should be kicked
	 * @return boolean value whether kick was successful
	 */
	public boolean kick(int clientId) {
		return server.kick(clientId);
	}

	/**
	 * Getter for kickMap
	 */
	public Map<Integer, Integer> getKickMap() {
		return kickMap;
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
