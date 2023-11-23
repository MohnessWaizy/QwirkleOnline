package org.NetworkInterface;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

public class NetClient {
	private ConcurrentLinkedQueue<Message> msgQueue;
	private Parser parser;
	private NetworkClient client = null;
	private Thread thread = null;

	/**
	 * @param port portnr to connect to
	 * @param iP   ip to connect to
	 */
	public NetClient(int port, String iP) {
		super();
		this.msgQueue = new ConcurrentLinkedQueue<Message>();
		this.parser = new Parser();
		client = new NetworkClient(port, this, iP);
		thread = new Thread(client);
		thread.start();
	}
	
	public synchronized boolean checkConnection() {
		short time = 0;
		while((client.getConnectionState() == ConnectionState.CONNECTION_BUILDING || 
				client.getConnectionState() == ConnectionState.NOT_STARTED) && 
				time < 4000) {
			try{
                Thread.sleep(1);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
			time++;
		}
		if(this.client.getConnectionState() == ConnectionState.CONNECTION_SUCCESSFULL) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @param msg Message that is received
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
	 * @param msg Message that should be transmitted
	 */
	public void sendMsg(Message msg) {
		client.sendMsg(parser.serialize(msg));
	}

	/**
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
