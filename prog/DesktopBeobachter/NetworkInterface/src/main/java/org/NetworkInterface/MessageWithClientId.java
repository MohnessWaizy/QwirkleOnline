package org.NetworkInterface;
import de.upb.swtpra1819interface.messages.Message;

public class MessageWithClientId {
	public long id;
	public Message msg;

	/**
	 * @param msg Message that was received
	 * @param id  clientId of the sender
	 */
	public MessageWithClientId(Message msg, long id) {
		super();
		this.id = id;
		this.msg = msg;
	}

}
