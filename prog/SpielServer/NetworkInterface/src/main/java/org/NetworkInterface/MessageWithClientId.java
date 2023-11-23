package org.NetworkInterface;

import de.upb.swtpra1819interface.messages.Message;

/**
 * 
 * Used to wrap the messages with the origin id
 *
 */
public class MessageWithClientId {
	private int clientId;
	private Message msg;

	/**
	 * @param msg Message that was received
	 * @param id  clientId of the sender
	 */
	public MessageWithClientId(Message msg, int clientId) {
		super();
		this.clientId = clientId;
		this.msg = msg;
	}

	public synchronized int getClientId() {
		return clientId;
	}

	public synchronized void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public synchronized Message getMsg() {
		return msg;
	}

	public synchronized void setMsg(Message msg) {
		this.msg = msg;
	}

}
